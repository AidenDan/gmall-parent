package com.gmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.ums.entity.MemberLevel;
import com.gmall.ums.mapper.MemberLevelMapper;
import com.gmall.ums.service.MemberLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
@Component
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevel> implements MemberLevelService {

}
