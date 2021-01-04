package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/24 15:30
 */
public interface SpecificationService {
    /**
    *根据cid查询商品组信息
    *@Author：ykym
    * @param: [cid]
    * @return: java.util.List<com.leyou.item.pojo.SpecGroup>
    * @Date:  17:04 2020/8/24
    */
    List<SpecGroup> queryGroupsByCid(Long cid);
    /**
     *根据gid查询规格参数
     *@Author：ykym
     * @param: [gid]
     * @return: org.springframework.http.ResponseEntity<java.util.List<com.leyou.item.pojo.SpecParm>>
     * @Date:  17:04 2020/8/24
     */
    List<SpecParam> queryParms(Long gid,Long cid,Boolean generic,Boolean searching);

    /**
    *根据cid查询specgroup以及specparm
    *@Author：ykym
    * @param: [id]
    * @return: java.util.List<com.leyou.item.pojo.SpecParam>
    * @Date:  20:47 2020/9/5
    */
    List<SpecGroup> querySpecsByCid(Long id);
}
