package com.gmall.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.gmall.constant.SysCacheConstant;
import com.gmall.to.CommonResult;
import com.gmall.ums.entity.Member;
import com.gmall.ums.service.MemberService;
import com.gmall.vo.ums.LoginResponseVo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-11 16:15:06
 */
@Slf4j
@Controller
@CrossOrigin
public class LoginController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Reference
    MemberService memberService;

    /* *
    * 用户登录后就通过accessToken查询用户的全部信息
    *
    * */
    @ResponseBody
    @GetMapping("/getUserInfo")
    public Object getUserInfo(@RequestParam(value = "accessToken") String accessToken){
        log.info("accessToken:{}", accessToken);
        //在redis中获取用户的信息
        String redisKey = SysCacheConstant.LOGIN_MEMBER+accessToken;
        String loginMember = stringRedisTemplate.opsForValue().get(redisKey);
        //把json字符串反序列化为Object
       // Gson gson = new Gson();
        //Map map = gson.fromJson(loginMember, Map.class);
        Member member = JSON.parseObject(loginMember, Member.class);
        //用户的密码和id不能传给前台页面
        member.setId(null);
        member.setPassword(null);
        return new CommonResult().success(member);
    }

    /* *
    * 属于本系统的登录功能
    * 这是一个前后台分离的项目，前端页面已经写好了登录页，后端只要实现功能就可以了
    *
    * */
    @ResponseBody
    @PostMapping("/applogin")
    public Object loginForGmall(@RequestParam("username") String username, @RequestParam("password") String password){
        Member member = memberService.login(username, password);
        if(member==null){
            //用户名或密码错误 获取一个登录失败的结果对象
            CommonResult result = new CommonResult().failed();
            result.setMessage("用户名或者密码错误");
            return result;
        }else {
            //登录成功，把用户信息存入redis，key为UUID，或jwt生成token
            String token = UUID.randomUUID().toString().replace("-", "");
            //把对象序列化为json字符串
            //Gson gson = new Gson();
            String jsonMember = JSON.toJSONString(member);//gson.toJson(member);
            stringRedisTemplate.
                    opsForValue().
                    set(SysCacheConstant.LOGIN_MEMBER+token,
                            jsonMember,
                            SysCacheConstant.LOGIN_MEMBER_TIMEOUT,
                            TimeUnit.MINUTES);
            LoginResponseVo loginResponseVo = new LoginResponseVo();
            //把member中的一部分信息复制到vo中
            BeanUtils.copyProperties(member, loginResponseVo);
            //注意带上令牌
            loginResponseVo.setAccessToken(token);
            return new CommonResult().success(loginResponseVo);
        }
    }





    /* *  * * *  */

    /* *
    * 拿到重定向的地址
    *  测试非前后台分离项目
    *
    * */
    @GetMapping("/login")
    public String login(@RequestParam(value = "redirect_url") String redirect_url,
                                    @CookieValue(value ="sso_user", required = false) String ssoUser,
                                    Model model){
        log.info("认证中心开始认证:{}, {}", redirect_url, ssoUser);
        //判断之前是否登陆过
        if(StringUtils.isEmpty(ssoUser)){
            //未登录过， 那么就去登录页面 带上这个最初的访问页面
            model.addAttribute("redirect_url", redirect_url);
            return "login";
        }else {
            //登陆过 就回到之前的地方，并且把当前ssouser或取得到的cookie以url方式传递给其他的域名(cookie同步)
            return "redirect:"+redirect_url+"?sso_user="+ssoUser;
        }
    }

    /* *
    * 初次登录系统
    * 测试非前后台分离项目
    * */
    @PostMapping("/doLogin")
    public String doLogin(String username,
                                          String password,
                                          HttpServletResponse response,
                                          HttpServletRequest request,
                                          String redirect_url){
        //模拟用户信息
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("email", username+"@qq.com");
        //用户登录后，把用户信息存入redis
        Gson gson = new Gson();
        System.out.println(gson.toJson(map));
        //把用户信息序列化为json字符串后存入redis，key为UUID
        String token = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(token, gson.toJson(map));
        //登录成功后还要做两件事
        //1、命里浏览器把当前的token保存为cookie----->sso_user=token,
        //2、命令浏览器重定向到最初访问的位置
        Cookie sso_user = new Cookie("sso_user", token);
        response.addCookie(sso_user);
        return "redirect:"+redirect_url+"?sso_user="+token;
    }
}
