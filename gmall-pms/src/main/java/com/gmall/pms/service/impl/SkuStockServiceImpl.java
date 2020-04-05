package com.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.pms.entity.SkuStock;
import com.gmall.pms.mapper.SkuStockMapper;
import com.gmall.pms.service.SkuStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
@Component
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements SkuStockService {

}
