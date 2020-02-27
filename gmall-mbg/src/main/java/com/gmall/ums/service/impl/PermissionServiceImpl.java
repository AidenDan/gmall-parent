package com.gmall.ums.service.impl;

import com.gmall.ums.entity.Permission;
import com.gmall.ums.mapper.PermissionMapper;
import com.gmall.ums.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户权限表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
