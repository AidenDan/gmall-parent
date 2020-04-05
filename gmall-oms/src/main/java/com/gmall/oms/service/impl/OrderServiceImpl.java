package com.gmall.oms.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gmall.cart.service.CartService;
import com.gmall.cart.vo.CartItem;
import com.gmall.constant.SysCacheConstant;
import com.gmall.oms.component.MemberComponent;
import com.gmall.oms.entity.Order;
import com.gmall.oms.mapper.OrderMapper;
import com.gmall.oms.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmall.sms.entity.Coupon;
import com.gmall.sms.service.CouponService;
import com.gmall.ums.entity.Member;
import com.gmall.ums.entity.MemberReceiveAddress;
import com.gmall.ums.service.MemberService;
import com.gmall.vo.OrderConfirmVo;
import com.gmall.vo.OrderCreateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2020-02-27
 */
@Service
@Component
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Reference
    MemberService memberService;
    @Reference
    CartService cartService;
    @Reference
    CouponService couponService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    MemberComponent memberComponent;


    @Override
    public OrderConfirmVo createOrder(Long id) {
        //获取上一步隐式传参带来的参数accessToken
        String accessToken = RpcContext.getContext().getAttachment("accessToken");
        //根据用户id信息获取用户信息
        List<MemberReceiveAddress> address = memberService.getMemberAddress(id);
        //根据用户的accessToken获取用户的购物项信息
         List<CartItem> cartItems =  cartService.getMemberCartItem(accessToken);
         //同理查询优惠券信息
        List<Coupon> coupons = couponService.getMemberCoupons(id);
        // 返回值要什么，我们就返回前台什么数据
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        orderConfirmVo.setAddresses(address);
        orderConfirmVo.setCoupons(coupons);
        orderConfirmVo.setItems(cartItems);
        //设置订单的防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        //给令牌加上业务的过期时间，10分钟
        token = token+ "_"+ System.currentTimeMillis()+"_"+60*10;
        //把防重令牌存入到redis中
        stringRedisTemplate.opsForSet().add(SysCacheConstant.ORDER_UNIQUE_TOKEN, token);
        orderConfirmVo.setOrderToken(token);

        //计算价格等
        orderConfirmVo.setCouponPrice(null);
        cartItems.forEach(cartItem -> {
            //计算商品的总件数
            orderConfirmVo.setCount(orderConfirmVo.getCount()+cartItem.getCount());
            //计算商品的总额
            orderConfirmVo.setProductTotalPrice(orderConfirmVo.getProductTotalPrice().add(cartItem.getTotalPrice()));
        });
        //运费是远程计算的
        orderConfirmVo.setTransPrice(new BigDecimal(new Random().nextInt(10)));

        return orderConfirmVo;
    }

    //先把数据模型理清楚，要返回哪些数据，先set好，再去数据库查
    @Override
    public OrderCreateVo createOrderFinally(BigDecimal totalPrice, Long addressId, String note) {
        OrderCreateVo orderCreateVo = new OrderCreateVo();
        //获取dubbo隐式传参的值 dubbo标签的所有属性都是关键字不能隐式传参。
        String accessToken = RpcContext.getContext().getAttachment("accessToken");
        //根据accessToken在redis中获取用户信息。这里我们也可以抽取一个组件出来
        Member member = memberComponent.getMemberByAccessToken(accessToken);
        //获取订单号，用IdWorker生成，mybatisPlus自带的工具
        String timeId = IdWorker.getTimeId();
        orderCreateVo.setOrderSn(timeId);
        //设置收货地址，
        orderCreateVo.setAddressId(addressId);
        //设置购物项
        List<CartItem> cartItems = cartService.getMemberCartItem(accessToken);
        orderCreateVo.setCartItemList(cartItems);
        //设置用户的id
        orderCreateVo.setMemberId(member.getId());
        //总价格
        orderCreateVo.setTotalPrice(totalPrice);
        //描述信息...
        orderCreateVo.setDetailInfo(cartItems.get(0).getName());
        return orderCreateVo;
    }
}














