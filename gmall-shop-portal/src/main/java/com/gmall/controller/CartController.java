package com.gmall.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.cart.service.CartService;
import com.gmall.cart.vo.CartItem;
import com.gmall.cart.vo.CartResponse;
import com.gmall.to.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-12 16:00:44
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference
    CartService cartService;

    //添加购物项去购物车
    @PostMapping("/addToCart")
    public Object addToCart(@RequestParam("skuId") Long skuId,
                                                @RequestParam(value = "num", defaultValue = "1") Integer num,
                                                @RequestParam(value = "cartKey", required = false) String cartKey,
                                                @RequestParam(value = "accessToken", required = false) String accessToken) throws ExecutionException, InterruptedException {
        CartResponse cartResponse = cartService.addToCart(skuId, num, cartKey, accessToken);
        return new CommonResult().success(cartResponse);
    }

    //更新购物车商品数量
    @PostMapping("/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuId", value = "商品的skuId"),
            @ApiImplicitParam(name = "num", value = "数量", defaultValue = "1"),
            @ApiImplicitParam(name = "cartKey", value = "离线购物车的key，可以没有"),
            @ApiImplicitParam(name = "accessToken", value = "登陆后的访问令牌，没登录就不用传")
    })
    public Object updateCart(@RequestParam("skuId") Long skuId,
                                                @RequestParam(value = "num", defaultValue = "1") Integer num,
                                                @RequestParam(value = "cartKey", required = false) String cartKey,
                                                @RequestParam(value = "accessToken", required = false) String accessToken) throws ExecutionException, InterruptedException {
        CartResponse cartResponse = cartService.updateCart(skuId, num, cartKey, accessToken);
        return new CommonResult().success(cartResponse);
    }

    //查询购物车
    @GetMapping("/list")
    public Object listCart( @RequestParam(value = "cartKey", required = false) String cartKey,
                                           @RequestParam(value = "accessToken", required = false) String accessToken) throws ExecutionException, InterruptedException {
        CartResponse cartResponse = cartService.listCart(cartKey, accessToken);
        return new CommonResult().success(cartResponse);
    }

    //删除购物车购物项
    @PostMapping("/del")
    public Object delCartItem(  @RequestParam("skuId") Long skuId,
                                                    @RequestParam(value = "cartKey", required = false) String cartKey,
                                                    @RequestParam(value = "accessToken", required = false) String accessToken) throws ExecutionException, InterruptedException {
        CartResponse cartResponse = cartService.delCartItem(skuId, cartKey, accessToken);
        return new CommonResult().success(cartResponse);
    }

    //清空购物车购物项
    @PostMapping("/clear")
    public Object clearCart(  @RequestParam(value = "cartKey", required = false) String cartKey,
                                                @RequestParam(value = "accessToken", required = false) String accessToken) throws ExecutionException, InterruptedException {
        CartResponse cartResponse = cartService.clearCart(cartKey, accessToken);
        return new CommonResult().success(cartResponse);
    }

    //复选框是否选中购物项，修改购物车中购物项的选中状态
    @ApiOperation(value = "复选框是否选中购物项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cartKey", value = "离线购物车的key，可以没有"),
            @ApiImplicitParam(name = "accessToken", value = "登陆后的访问令牌，没登录就不用传"),
            @ApiImplicitParam(name = "skuIds", value ="所有操作商品的skuId的集合，多个商品的id用逗号分隔开" ),
            @ApiImplicitParam(name = "ops", value ="1:选中，0:未选中" )
    })
     @PostMapping("/check")
    public Object checkCart( @RequestParam(value = "cartKey", required = false) String cartKey,
                                                @RequestParam(value = "accessToken", required = false) String accessToken,
                                                @RequestParam(value = "skuIds") String skuIds,
                                                @RequestParam(value = "ops", defaultValue = "1") Integer ops) throws ExecutionException, InterruptedException {
        CartResponse cartResponse = cartService.checkCart(cartKey, accessToken, skuIds, ops);
        return new CommonResult().success(cartResponse);
    }
}

























