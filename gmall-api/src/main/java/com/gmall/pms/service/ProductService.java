package com.gmall.pms.service;

import com.gmall.pms.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gmall.vo.PageInfoVo;
import com.gmall.vo.product.PmsProductParam;
import com.gmall.vo.product.PmsProductQueryParam;

import java.util.List;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
public interface ProductService extends IService<Product> {

    /**
     *
     *
     * @description: 把查到的商品信息封装到PageInfoVo中,前台传递到服务器的参数封装到PmsProductQueryParam
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-2-28 16:04:30
     */
    PageInfoVo productPageInfo(PmsProductQueryParam param);

    /**
     *
     *
     * @description: 保存商品信息
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-2 18:14:46
     */
    void saveProduct(PmsProductParam productParam);

    /**
     *
     *
     * @description: 更新商品的上架下架状态
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-3 17:19:11
     */
    void updateProductPublishStatus(List<Long> ids, Integer publishStatus);
}
