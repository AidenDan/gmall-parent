package com.gmall.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gmall.mapper.ProductMapper;
import com.gmall.pms.entity.Product;
import com.gmall.search.SearchProductService;
import com.gmall.to.es.EsProduct;
import com.gmall.vo.search.SearchParam;
import com.gmall.vo.search.SearchResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-3 20:37:22
 */
@Service
@Component
public class SearchProductServiceImpl implements SearchProductService {
    @Autowired(required = false)
    ProductMapper productMapper;

    @Override
    public SearchResponse searchProduct(SearchParam searchParam) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(searchParam.getKeyword())){
            queryWrapper.like("name", searchParam.getKeyword());
        }
        IPage<Product> page = productMapper.selectPage(new Page<Product>(searchParam.getPageNum(), searchParam.getPageSize()), queryWrapper);
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setPageNum((int)page.getCurrent());
        searchResponse.setPageSize(searchParam.getPageSize());
        searchResponse.setTotal(page.getTotal());
        List<EsProduct> list = new ArrayList<>();
        //在数据库中查到的商品信息
        List<Product> productList = page.getRecords();
        productList.forEach(product -> {
            EsProduct esProduct = new EsProduct();
            BeanUtils.copyProperties(product, esProduct);
            list.add(esProduct);
        });
        searchResponse.setProducts(list);
        return searchResponse;
    }
}
