package com.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gmall.pms.entity.ProductAttributeCategory;
import com.gmall.pms.mapper.ProductAttributeCategoryMapper;
import com.gmall.pms.service.ProductAttributeCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Service
@Component
public class ProductAttributeCategoryServiceImpl extends ServiceImpl<ProductAttributeCategoryMapper, ProductAttributeCategory> implements ProductAttributeCategoryService {
    @Autowired
    ProductAttributeCategoryMapper productAttributeCategoryMapper;

    @Override
    public PageInfoVo listPageInfo(Integer pageNum, Integer pageSize) {
        IPage<ProductAttributeCategory> page = productAttributeCategoryMapper.selectPage(new Page<ProductAttributeCategory>(pageNum, pageSize), null);
        return new PageInfoVo(page.getTotal(), page.getPages(), pageSize.longValue(), page.getRecords(), page.getCurrent());
    }
}
