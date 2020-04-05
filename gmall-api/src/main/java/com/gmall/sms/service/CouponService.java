package com.gmall.sms.service;

import com.gmall.sms.entity.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 优惠卷表 服务类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
public interface CouponService extends IService<Coupon> {

    //根据用户id获取用户的优惠卷信息
    List<Coupon> getMemberCoupons(Long id);
}
