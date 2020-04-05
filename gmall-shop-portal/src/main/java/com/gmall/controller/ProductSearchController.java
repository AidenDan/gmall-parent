package com.gmall.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.search.SearchProductService;
import com.gmall.vo.search.SearchParam;
import com.gmall.vo.search.SearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品检索的controller
 */
@Slf4j
@CrossOrigin
@Api(tags = "检索功能")
@RestController
public class ProductSearchController {

    @Reference
    SearchProductService searchProductService;

    @ApiOperation("商品检索")
    @GetMapping("/search")
    public SearchResponse productSearchResponse(SearchParam searchParam){

        /**
         * 检索商品
         */
        SearchResponse searchResponse = searchProductService.searchProduct(searchParam);
        log.info("前台传递过来的参数:{}", searchParam);

        return searchResponse;
    }

}
