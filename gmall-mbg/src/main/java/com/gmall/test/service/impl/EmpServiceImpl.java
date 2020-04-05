package com.gmall.test.service.impl;

import com.gmall.test.entity.Emp;
import com.gmall.test.mapper.EmpMapper;
import com.gmall.test.service.EmpService;
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
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp> implements EmpService {

}
