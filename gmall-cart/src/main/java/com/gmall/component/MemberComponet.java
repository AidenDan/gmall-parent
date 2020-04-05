package com.gmall.component;

import com.alibaba.fastjson.JSON;
import com.gmall.cart.vo.CartItem;
import com.gmall.cart.vo.CartResponse;
import com.gmall.cart.vo.UserCartKey;
import com.gmall.constant.CartConstant;
import com.gmall.constant.SysCacheConstant;
import com.gmall.ums.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author Aiden
 * @version 1.0
 * @description 封装一些判断用户是否已经登录的方法
 * @date 2020-3-12 16:33:52
 */
@Component
public class MemberComponet {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public Member getMemberByAccessToken(String accessToken) {
        String abc = SysCacheConstant.LOGIN_MEMBER + accessToken;
        System.err.println(abc);
        String jsonMember = stringRedisTemplate.opsForValue().get(SysCacheConstant.LOGIN_MEMBER + accessToken);
        System.err.println(jsonMember);
        return JSON.parseObject(jsonMember, Member.class);
    }

    //代码重构，判断购物车到底用哪个key
    public UserCartKey getCartKey(String accessToken, String cartKey) {
        UserCartKey userCartKey = new UserCartKey();
        String FINAL_KEY = "";
        Member member = getMemberByAccessToken(accessToken);
        if (member != null) {
            //用户登陆了，购物车的key为用户id标识
            FINAL_KEY = CartConstant.USER_CART_KEY_PREFIX + member.getId();
            userCartKey.setId(member.getId());
            userCartKey.setIsLogin(true);
            userCartKey.setFinalKey(FINAL_KEY);
            return userCartKey;
        }
        if (!StringUtils.isEmpty(cartKey)) {
            //用户没有登录，但以前已经有了离线购物车
            FINAL_KEY = CartConstant.TEMP_CART_KEY_PREFIX + cartKey;
            userCartKey.setIsLogin(false);
            userCartKey.setFinalKey(FINAL_KEY);
            return userCartKey;
        }
        //3、如果以上都没有，说明刚来(第一次来)，分配一个临时购物车
        String newCartKey = UUID.randomUUID().toString().replace("-", "");
        FINAL_KEY = CartConstant.TEMP_CART_KEY_PREFIX + newCartKey;
        userCartKey.setIsLogin(false);
        userCartKey.setFinalKey(FINAL_KEY);
        userCartKey.setNewCartKey(newCartKey);
        return userCartKey;
    }
}
