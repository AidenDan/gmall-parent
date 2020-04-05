package com.gmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gmall.ums.entity.Member;
import com.gmall.ums.entity.MemberReceiveAddress;
import com.gmall.ums.mapper.MemberMapper;
import com.gmall.ums.mapper.MemberReceiveAddressMapper;
import com.gmall.ums.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
@Component
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    MemberReceiveAddressMapper memberReceiveAddressMapper;

    @Override
    public Member login(String username, String password) {
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        return memberMapper.selectOne(new QueryWrapper<Member>().
                eq("username", username).
                eq("password", md5DigestAsHex));
    }

    //根据用户id获取用户的收获地址
    @Override
    public List<MemberReceiveAddress> getMemberAddress(Long id) {
        //查出当前用户的收获地址
        return memberReceiveAddressMapper.selectList(new QueryWrapper<MemberReceiveAddress>().eq("member_id", id));
    }
}
