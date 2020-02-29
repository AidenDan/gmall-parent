package com.gmall.ums.service;

import com.gmall.ums.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
public interface AdminService extends IService<Admin> {

    /* *
    * 用户登录
    *
    * */
    Admin login(String username, String password);

    /* *
    * 获取用户信息
    *
    * */
    Admin getUserInfo(String username);

    /* *
    * 用户注册
    * */
    Admin register(Admin admin);
}
