package com.gmall.constant;

/**
 * @author Aiden
 * @version 1.0
 * @description  这个类中存放系统中要使用的常量
 * @date 2020-3-2 12:42:10
 */
public class SysCacheConstant {

    //系统商品菜单常量
    public static final String CATEGORY_MENU_CACHE_KEY= "sys_category";
    //用户信息常量 login:member:token={userObj}
    public static final String LOGIN_MEMBER="login:member:";
    //用户登录的过期时间30分钟
    public static final Long LOGIN_MEMBER_TIMEOUT=30L;
    //用户提交确认订单防重令牌
    public static final String ORDER_UNIQUE_TOKEN="order:unique:token:";
}
