package com.gmall.controller;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.gmall.constant.SysCacheConstant;
import com.gmall.oms.service.OrderService;
import com.gmall.to.CommonResult;
import com.gmall.ums.entity.Member;
import com.gmall.vo.OrderConfirmVo;
import com.gmall.vo.OrderCreateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-16 12:19:24
 */
@RestController
@Api(tags = "订单服务")
@RequestMapping("/order")
public class OrderController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    OrderService orderService;

    /* *
    * 选中购物项后，点击确认后来到订单确认页面
    * 返回到此页面的数据如下
    * 1、当前用户的可选送地址列表
    * 2、当前购物车选中的商品信息
    * 3、可用的优惠券信息
    * 4、支付、配送、发票方式信息
    * 5、当信息确认完成后下一步要提交订单。我们必须做防重复验证[接口幂等性设计]
    *
    * */
    @GetMapping("/confirm")
    public Object confirmOrder(@RequestParam("accessToken") String accessToken){
        String memberJson = stringRedisTemplate.opsForValue().get(SysCacheConstant.LOGIN_MEMBER + accessToken);
        if(StringUtils.isEmpty(accessToken)|| StringUtils.isEmpty(memberJson)){
            CommonResult failed = new CommonResult().failed();
            failed.setMessage("用户未登录。。。");
            return failed;
        }
        Member member = JSON.parseObject(memberJson, Member.class);

        //dubbo的RPC隐式传参：setAttachment保存一下下一个远程服务需要的参数
        RpcContext.getContext().setAttachment("accessToken", accessToken);
        //调用下一个远程服务
        OrderConfirmVo orderConfirmVo = orderService.createOrder(member.getId());
        return new CommonResult().success(orderConfirmVo);
    }

    /* *
    * 点击确认订单，就跳转到支付页面
    *  这个页面会生成订单号
    * 创建订单的时候必须用到确认订单的那些数据
    * 订单的总额
    * 用户登录的token
    * 收获地址
    * totalPrice是为了比较价格
    * */
    @ApiOperation("确认订单来到支付页面")
    @PostMapping("/create")
    public Object createOrder(@RequestParam("totalPrice") BigDecimal totalPrice,
                                                   @RequestParam("accessToken") String accessToken,
                                                   @RequestParam("addressId") Long addressId,
                                                   @RequestParam(value = "note", required = false) String note){
        //创建订单要生成订单和订单项(购物车中的商品)；
        RpcContext.getContext().setAttachment("accessToken", accessToken);
        OrderCreateVo orderCreateVo =  orderService.createOrderFinally(totalPrice, addressId, note);
        return "";
    }
}






















