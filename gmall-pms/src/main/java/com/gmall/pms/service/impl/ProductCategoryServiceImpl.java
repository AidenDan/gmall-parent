package com.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gmall.constant.SysCacheConstant;
import com.gmall.pms.entity.ProductCategory;
import com.gmall.pms.mapper.ProductCategoryMapper;
import com.gmall.pms.service.ProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmall.vo.PageInfoVo;
import com.gmall.vo.product.PmsProductCategoryWithChildrenItem;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
@Slf4j
@Component
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
    @Autowired
    RedisTemplate<Object , Object> redisTemplate;

    @Autowired
    ProductCategoryMapper productCategoryMapper;

    @Override
    public List<PmsProductCategoryWithChildrenItem> pmsProductCategoryWithChildrenItem(Integer i) {
        //先查询缓存中是否有
        Object cacheMenu = redisTemplate.opsForValue().get(SysCacheConstant.CATEGORY_MENU_CACHE_KEY);
        List<PmsProductCategoryWithChildrenItem> itemList;
        if(cacheMenu !=null){
            //缓存中有
            log.debug("命中缓存");
            itemList = (List<PmsProductCategoryWithChildrenItem>)cacheMenu;
        }else {
            itemList = productCategoryMapper.pmsProductCategoryWithChildrenItem(i);
            redisTemplate.opsForValue().set(SysCacheConstant.CATEGORY_MENU_CACHE_KEY, itemList);
        }
        return itemList;
    }
}
