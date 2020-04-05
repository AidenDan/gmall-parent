package com.gmall.pms.service.impl;

import com.gmall.pms.entity.ProductAttributeValue;
import com.gmall.pms.mapper.ProductAttributeValueMapper;
import com.gmall.pms.service.ProductAttributeValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 存储产品参数信息的表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-03-01
 */
@Service
public class ProductAttributeValueServiceImpl extends ServiceImpl<ProductAttributeValueMapper, ProductAttributeValue> implements ProductAttributeValueService {

}
