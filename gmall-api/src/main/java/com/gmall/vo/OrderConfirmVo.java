package com.gmall.vo;

import com.gmall.cart.vo.CartItem;
import com.gmall.sms.entity.Coupon;
import com.gmall.ums.entity.MemberReceiveAddress;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Aiden
 * @version 1.0
 * @description  订单确认的vo，订单要显示什么数据，我们就返回什么数据
 * @date 2020-3-16 12:24:42
 */
@Data
public class OrderConfirmVo implements Serializable {
    //购物项
    List<CartItem> items;
    //收获地址信息
    List<MemberReceiveAddress> addresses;
    //优惠券信息
    List<Coupon> coupons;
    //其他的支付、配送方式等

    //设置一个订单防重令牌
    String orderToken;
    //订单总额=商品总额+运费-优惠卷减免
    private BigDecimal totalPrice=new BigDecimal("0");
    //总商品数
    private Integer count=0;
    //优惠卷减免
    private BigDecimal couponPrice=new BigDecimal("10");
    //运费
    private BigDecimal transPrice=new BigDecimal("10");
    //商品的总额
    private BigDecimal productTotalPrice=new BigDecimal("0");
}
