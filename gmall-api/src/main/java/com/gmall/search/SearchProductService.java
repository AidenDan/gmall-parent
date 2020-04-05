package com.gmall.search;
import com.gmall.vo.search.SearchParam;
import com.gmall.vo.search.SearchResponse;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-3 20:36:12
 */
public interface SearchProductService {

   SearchResponse searchProduct(SearchParam searchParam);
}
