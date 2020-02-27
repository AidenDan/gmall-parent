package com.gmall.pms.service.impl;

import com.gmall.pms.entity.ProductLadder;
import com.gmall.pms.mapper.ProductLadderMapper;
import com.gmall.pms.service.ProductLadderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品阶梯价格表(只针对同商品) 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
public class ProductLadderServiceImpl extends ServiceImpl<ProductLadderMapper, ProductLadder> implements ProductLadderService {

}
