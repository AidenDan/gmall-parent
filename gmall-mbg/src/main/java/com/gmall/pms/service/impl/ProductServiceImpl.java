package com.gmall.pms.service.impl;

import com.gmall.pms.entity.Product;
import com.gmall.pms.mapper.ProductMapper;
import com.gmall.pms.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
