package com.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gmall.pms.entity.ProductAttribute;
import com.gmall.pms.mapper.ProductAttributeMapper;
import com.gmall.pms.service.ProductAttributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
@Component
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {
    @Autowired
    ProductAttributeMapper productAttributeMapper;

    @Override
    public PageInfoVo getProductAttributeOrParam(Long cid, Integer type, Integer pageNum, Integer pageSize) {
        QueryWrapper<ProductAttribute> queryWrapper = new QueryWrapper<ProductAttribute>().eq("product_attribute_category_id", cid).eq("type", type);
        IPage<ProductAttribute> page = productAttributeMapper.selectPage(new Page<ProductAttribute>(pageNum, pageSize), queryWrapper);
        return new PageInfoVo(page.getTotal(), page.getPages(),pageSize.longValue(),page.getRecords(),page.getCurrent());
    }
}
