package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/24 15:37
 */
@RequestMapping("spec")
public interface SpecificationApi {
    /**
    *根据cid查询group
    *@Author：ykym
    * @param: [cid]
    * @return: org.springframework.http.ResponseEntity<java.util.List<com.leyou.item.pojo.SpecGroup>>
    * @Date:  0:17 2020/9/7
    */
    @GetMapping("groups/{cid}")
    public List<SpecGroup> queryGroupsByCid(@PathVariable("cid")Long cid);

    /**
    *查询规格参数
    *@Author：ykym
    * @param: [gid]
    * @return: org.springframework.http.ResponseEntity<java.util.List<com.leyou.item.pojo.SpecParm>>
    * @Date:  17:04 2020/8/24
    */
    @GetMapping("params")
    public List<SpecParam> queryParms(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "generic",required = false)Boolean generic,
            @RequestParam(value = "searching",required = false)Boolean search
    );

    /**
     *查询组消息以及组内规格参数，放入组内的集合
     *@Author：ykym
     * @param:
     * @return:
     * @Date:  20:43 2020/9/5
     */
    @GetMapping("{cid}")
   public List<SpecGroup> querySpecsByCid(@PathVariable("cid") Long cid);
}
