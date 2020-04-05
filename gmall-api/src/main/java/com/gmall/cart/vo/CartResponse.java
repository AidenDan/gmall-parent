package com.gmall.cart.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Aiden
 * @version 1.0
 * @description 添加购物项成功后的返回类型
 * @date 2020-3-12 16:17:59
 */
@Data
public class CartResponse implements Serializable {
    //整个购物车
    private Cart cart;
    //某项购物项
    private CartItem cartItem;
    private String cartKey;
}
