package com.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gmall.pms.entity.ProductCategory;
import com.gmall.pms.mapper.ProductCategoryMapper;
import com.gmall.pms.service.ProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Component
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
    @Autowired
    ProductCategoryMapper productCategoryMapper;

    @Override
    public PageInfoVo cateGoryInfo() {
        List<ProductCategory> list = productCategoryMapper.selectList(null);
        PageInfoVo pageInfoVo = new PageInfoVo();
        pageInfoVo.setList(list);
        return pageInfoVo;
    }
}
