package com.gmall.pms.mapper;

import com.gmall.pms.entity.ProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gmall.vo.product.PmsProductCategoryWithChildrenItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 产品分类 Mapper 接口
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    List<PmsProductCategoryWithChildrenItem> pmsProductCategoryWithChildrenItem(@Param("i") Integer i);
}
