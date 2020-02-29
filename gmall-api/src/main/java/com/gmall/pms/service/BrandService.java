package com.gmall.pms.service;

import com.gmall.pms.entity.Brand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gmall.vo.PageInfoVo;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
public interface BrandService extends IService<Brand> {

    /**
     *
     *
     * @description:  按条件查询商品信息
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-2-28 18:35:35
     */
    PageInfoVo brandPageInfo(String keyword, Integer pageNum, Integer pageSize);
}
