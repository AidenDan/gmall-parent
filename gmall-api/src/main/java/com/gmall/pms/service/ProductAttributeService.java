package com.gmall.pms.service;

import com.gmall.pms.entity.ProductAttribute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gmall.vo.PageInfoVo;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
public interface ProductAttributeService extends IService<ProductAttribute> {

    PageInfoVo getProductAttributeOrParam(Long cid, Integer type, Integer pageNum, Integer pageSize);
}
