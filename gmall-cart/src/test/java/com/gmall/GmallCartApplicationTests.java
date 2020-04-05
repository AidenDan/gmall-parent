package com.gmall;

import com.alibaba.fastjson.JSON;
import com.gmall.cart.vo.Cart;
import com.gmall.cart.vo.CartItem;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class GmallCartApplicationTests {

    @Autowired
    RedissonClient redissonClient;

    @Test
    void contextLoads() {
        //测试购物车总价计算是否正确
        //购物项一
        CartItem cartItem1 = new CartItem();
        cartItem1.setCount(2);
        cartItem1.setPrice(new BigDecimal("11.3"));

        //购物项二
        CartItem cartItem2= new CartItem();
        cartItem2.setCount(3);
        cartItem2.setPrice(new BigDecimal("21.3"));
        //把购物项添加到购物车
        List<CartItem> cartItemList = Arrays.asList(cartItem1, cartItem2);
        Cart cart = new Cart();
        cart.setCartItemList(cartItemList);

        System.out.println(cart.getCount());
        System.out.println(cart.getTotalPrice());

    }

    //使用Redisson提供的分布式集合
    @Test
    public void useRedissonMap(){
        //传入一个分布式集合的名字
        RMap<String, String> rMap = redissonClient.getMap("cart");
        CartItem cartItem = new CartItem();
        cartItem.setPrice(new BigDecimal("19.99"));
        cartItem.setCount(2);
        cartItem.setSkuId(1L);
        String jsonString = JSON.toJSONString(cartItem);
        rMap.put("2", jsonString);
    }

    //从分布式集合中取出map数据
    @Test
    public void getDataFromRedissonMap(){
        RMap<String, String> rMap = redissonClient.getMap("cart");
        String o = rMap.get("2");
        System.out.println(o);
        //移除购物车中的数据
        rMap.remove("1");
    }
}


















