package com.gmall.oms.service;

import com.gmall.oms.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gmall.vo.OrderConfirmVo;
import com.gmall.vo.OrderCreateVo;

import java.math.BigDecimal;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2020-02-27
 */
public interface OrderService extends IService<Order> {


    /**
     *
     *
     * @description: 创建订单确认
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-16 13:08:18
     */
    OrderConfirmVo createOrder(Long id);

    /**
     *
     *
     * @description: 确认订单
     * @param null
     * @param totalPrice
     * @param addressId
     * @return:
     * @author: Aiden
     * @time: 2020-3-16 20:55:07
     */

    OrderCreateVo createOrderFinally(BigDecimal totalPrice, Long addressId, String note);
}
