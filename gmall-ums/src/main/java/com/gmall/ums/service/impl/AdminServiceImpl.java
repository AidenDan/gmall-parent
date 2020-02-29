package com.gmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gmall.ums.entity.Admin;
import com.gmall.ums.mapper.AdminMapper;
import com.gmall.ums.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
@Component
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    /* *
    * 登陆的服务
    * String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());Spring自带MD5加密
    * */
    @Override
    public Admin login(String username, String password) {
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>().eq("username", username).eq("password", md5DigestAsHex);
        return adminMapper.selectOne(queryWrapper);
    }

    /* *
    * 获取到登录用户的信息
    *
    * */
    @Override
    public Admin getUserInfo(String username) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>().eq("username", username);
        return adminMapper.selectOne(queryWrapper);
    }

    /* *
    * 用户注册
    * */
    @Override
    public Admin register(Admin admin) {
        int insert = adminMapper.insert(admin);
        if(insert>0){
            return admin;
        }
        return null;
    }
}
