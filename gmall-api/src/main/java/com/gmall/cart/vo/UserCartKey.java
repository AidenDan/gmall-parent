package com.gmall.cart.vo;

import lombok.Data;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-14 13:54:57
 */
@Data
public class UserCartKey {
    private Long id;
    private Boolean isLogin;
    private String finalKey;
    //用户初次登录购物车，随机一个新购物车key
    private String newCartKey;
}
