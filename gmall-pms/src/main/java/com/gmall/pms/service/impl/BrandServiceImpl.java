package com.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gmall.pms.entity.Brand;
import com.gmall.pms.mapper.BrandMapper;
import com.gmall.pms.service.BrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Component
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Autowired(required = false)
    BrandMapper brandMapper;

    /* *
    * 按条件查询品牌信息
    * */
    @Override
    public PageInfoVo brandPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        QueryWrapper<Brand> queryWrapper = null;
        if(!StringUtils.isEmpty(keyword)){
            queryWrapper = new QueryWrapper<Brand>().like("name", keyword);
        }
        IPage<Brand> page = brandMapper.selectPage(new Page<Brand>(pageNum, pageSize), queryWrapper);
        return new PageInfoVo(page.getTotal(), page.getPages(), pageSize.longValue(), page.getRecords(), page.getCurrent());
    }
}
