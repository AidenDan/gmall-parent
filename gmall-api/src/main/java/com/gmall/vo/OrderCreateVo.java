package com.gmall.vo;

import com.gmall.cart.vo.CartItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-16 20:46:55
 */
@Data
public class OrderCreateVo {
    //订单号
    private String orderSn;
    //订单总额
    private BigDecimal totalPrice;
    //用户的收获地址
    private Long  addressId;
    //订单详情描述
    private String detailInfo;
    //会员的id
    private Long memberId;
    //购买的商品
    private List<CartItem> cartItemList;
}
