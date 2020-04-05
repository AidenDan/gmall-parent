package com.gmall.cart.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-12 13:16:26
 */
@Setter
public class Cart implements Serializable {
    //以下为购物车的基本信息
    //1、所有的购物项
    @Getter
    List<CartItem> cartItemList;
    //2、购物车中商品的总数
    private Integer count;
    //3、已选中商品的总价
    private BigDecimal totalPrice;

    //计算商品的总件数
    public Integer getCount() {
        //lambda表达式中要用原子级别的数，否则不能保证执行顺序 初始值为0
        AtomicInteger atomicInteger = new AtomicInteger(0);
        cartItemList.forEach(cartItem -> {
            //先取值后添加，每一个购物项中可有多件商品
            atomicInteger.getAndAdd(cartItem.getCount());
        });
        count = atomicInteger.get();
        return count;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal bigDecimal = new BigDecimal(0);
        for (CartItem cartItem : cartItemList) {
            //把每个购物项的总价格进行累加
            bigDecimal = bigDecimal.add(cartItem.getTotalPrice());
        }
        return bigDecimal;
    }
}
