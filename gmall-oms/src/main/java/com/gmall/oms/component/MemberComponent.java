package com.gmall.oms.component;

import com.alibaba.fastjson.JSON;
import com.gmall.ums.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-16 21:34:50
 */
@Component
public class MemberComponent {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public Member getMemberByAccessToken(String accessToken){
        if(!StringUtils.isEmpty(accessToken)){
            String memberJson = stringRedisTemplate.opsForValue().get(accessToken);
            return JSON.parseObject(memberJson, Member.class);
        }
        return null;
    }
}
