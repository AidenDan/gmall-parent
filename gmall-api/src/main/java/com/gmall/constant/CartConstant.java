package com.gmall.constant;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-12 16:51:31
 */
public class CartConstant {
    //离线购物车前缀,后面加cartKey
    public final static String TEMP_CART_KEY_PREFIX = "cart:temp:";
    //在线购物车前缀,后面加用户的id
    public final static String USER_CART_KEY_PREFIX="cart:user:";
    //购物车在redis中存储哪些被选中用的key
    public final static String CART_CHECKED_KEY="checked";
}
