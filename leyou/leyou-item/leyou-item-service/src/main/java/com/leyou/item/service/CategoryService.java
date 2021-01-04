package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/12 14:56
 */

public interface CategoryService {
    /**
    *由父节点查询子节点
    *@Author：ykym
    * @param: [pid]
    * @return: java.util.List<com.leyou.item.pojo.Category>
    * @Date:  17:49 2020/8/13
    */
    public List<Category> queryCategoriesByPid(Long pid);

    public List<Category> queryByBrandId(Long bid);
    /**
    *根据分类cid查询名称
    *@Author：ykym
    * @param: [asList]
    * @return: java.util.List<java.lang.String>
    * @Date:  15:56 2020/9/2
    */
    List<String> queryNamesByIds(List<Long> asList);
}
