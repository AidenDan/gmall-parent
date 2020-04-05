package com.gmall.cart.service;

import com.gmall.cart.vo.CartItem;
import com.gmall.cart.vo.CartResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-12 15:57:43
 */
public interface CartService {
    /**
     *
     *
     * @description: 添加购物项
     * @param null
     * @param
     * @param
     * @return:
     * @author: Aiden
     * @time: 2020-3-12 16:15:58
     */
     CartResponse addToCart(Long skuId, Integer num, String cartKey, String accessToken) throws ExecutionException, InterruptedException;

     /**
      *
      *
      * @description: 更新购物车
      * @param null
      * @return:
      * @author: Aiden
      * @time: 2020-3-14 13:27:49
      */

    CartResponse updateCart(Long skuId, Integer num, String cartKey, String accessToken);

    /**
     *
     *
     * @description: 查询购物车
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-14 15:13:50
     */

    CartResponse listCart(String cartKey, String accessToken);

    /**
     *
     *
     * @description: 删除购物车购物项
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-14 17:47:22
     */

    CartResponse delCartItem(Long skuId, String cartKey, String accessToken);

    /**
     *
     *
     * @description: 清空购物车
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-14 18:12:34
     */

    CartResponse clearCart(String cartKey, String accessToken);

    /**
     *
     *
     * @description: 复选框是否选中购物项
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-15 09:58:03
     */

    CartResponse checkCart(String cartKey, String accessToken, String skuIds, Integer ops);

    /**
     *
     *
     * @description: 根据用户的token查询查询用户的购物项信息
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-16 18:46:55
     */

    List<CartItem> getMemberCartItem(String accessToken);
}













