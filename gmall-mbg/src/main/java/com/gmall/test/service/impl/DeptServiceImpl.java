package com.gmall.test.service.impl;

import com.gmall.test.entity.Dept;
import com.gmall.test.mapper.DeptMapper;
import com.gmall.test.service.DeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-03-02
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

}
