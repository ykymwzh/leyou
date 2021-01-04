package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/14 11:28
 */
public interface BrandService {
    PageResult<Brand> queryBrandsByPage(String key, Integer page,Integer rows,String sortBy, Boolean desc);

    void saveBrand(Brand brand, List<Long> cids);

    void deleteByBrandId(Long did);
    /**
    *根据分类cid查询品牌
    *@Author：ykym
    * @param: [cid]
    * @return: java.util.List<com.leyou.item.pojo.Brand>
    * @Date:  0:07 2020/8/26
    */
    List<Brand> queryBrandsByCid(Long cid);

    Brand queryBrandById(Long id);
}
