package com.gmall.ums.service.impl;

import com.gmall.ums.entity.Role;
import com.gmall.ums.mapper.RoleMapper;
import com.gmall.ums.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
