package com.gmall.pms.service;

import com.gmall.pms.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gmall.vo.PageInfoVo;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    PageInfoVo cateGoryInfo();
}
