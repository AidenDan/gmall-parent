package com.gmall.ums.service.impl;

import com.gmall.ums.entity.Admin;
import com.gmall.ums.mapper.AdminMapper;
import com.gmall.ums.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
