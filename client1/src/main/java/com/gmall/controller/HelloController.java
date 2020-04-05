package com.gmall.controller;

import com.gmall.config.SsoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-11 15:16:13
 */
@CrossOrigin
@Controller
public class HelloController {
    @Autowired
    SsoConfig ssoConfig;

    /* *
    *  @CookieValue用于获取前台传递过来的cookie值
    *   cookie不能跨域传递，可以通过url参数传递
    *  测试非前后台分离项目
    *  模拟客户端
    *
    * */
    @GetMapping("/")
    public String index(Model model,
                        @CookieValue(value = "sso_user", required = false) String ssoUserCookie,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam(value = "sso_user", required = false) String ssoUserParam ){
        if(!StringUtils.isEmpty(ssoUserParam)){
            //没有调用认证服务器登录后跳转回来, 说明远程登陆了
            Cookie sso_user = new Cookie("sso_user", ssoUserParam);
            response.addCookie(sso_user);
            return "index";
        }
        StringBuffer requestURL = request.getRequestURL();
        //判断是否已经登录
        if(StringUtils.isEmpty(ssoUserCookie)){
            //没登录 重定向到登录服务器
            //重定向成了就去xxxx?redirect_url=
            return "redirect:"+ssoConfig.getUrl()+ssoConfig.getLoginpath()+"?redirect_url="+requestURL.toString();
        }else {
            //登陆了
            model.addAttribute("loginUser", "曹操");
            return "index";
        }
    }
}
