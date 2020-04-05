package com.gmall.pms.service;

import com.gmall.pms.entity.ProductAttributeCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gmall.vo.PageInfoVo;

/**
 * <p>
 * 产品属性分类表 服务类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
public interface ProductAttributeCategoryService extends IService<ProductAttributeCategory> {

    PageInfoVo listPageInfo(Integer pageNum, Integer pageSize);
}
