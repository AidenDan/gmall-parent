package com.gmall.cart.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Aiden
 * @version 1.0
 * @description  购物车中的购物项
 * @date 2020-3-12 12:57:22
 */
@Setter
public class CartItem implements Serializable {
    //当前购物项的基本信息
    @Getter
    private Long skuId;
    @Getter
    private String name;
    @Getter
    private String skuCode;
    @Getter
    private Integer stock;
    @Getter
    private String sp1;
    @Getter
    private String sp2;
    @Getter
    private String sp3;
    @Getter
    private String pic;
    @Getter
    private BigDecimal price;
    @Getter
    private BigDecimal promotionPrice;
    //以上为购物项的基本信息

    //购物项的选中状态，，默认是true
    @Getter
    private Boolean check = true;
    //当前购物项有多少件商品
    @Getter
    private Integer count;
    //当前购物项总价 涉及到计算有小数时都用BigDecimal来保证精度
    private BigDecimal totalPrice;
    //因为这个 totalPrice要我们自己计算得到，所以我们重写他
    public BigDecimal getTotalPrice() {
        totalPrice = this.price.multiply(new BigDecimal(this.count.toString()));
        return totalPrice;
    }
}
