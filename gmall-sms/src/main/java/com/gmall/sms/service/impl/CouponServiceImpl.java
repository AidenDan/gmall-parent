package com.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gmall.sms.entity.Coupon;
import com.gmall.sms.mapper.CouponMapper;
import com.gmall.sms.service.CouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 优惠卷表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
@Component
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {
    @Autowired
    CouponMapper couponMapper;

    @Override
    public List<Coupon> getMemberCoupons(Long id) {
        return couponMapper.selectList(new QueryWrapper<Coupon>().eq("", id));
    }
}
