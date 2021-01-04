package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.rmi.runtime.NewThreadAction;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/24 15:37
 */
@Controller
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    /**
    *通过cid查询商品组信息
    *@Author：ykym
    * @param: [cid]
    * @return: org.springframework.http.ResponseEntity<java.util.List<com.leyou.item.pojo.SpecGroup>>
    * @Date:  17:03 2020/8/24
    */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid")Long cid){
        List<SpecGroup> groups = specificationService.queryGroupsByCid(cid);

        if (CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }
    /**
    *根据gid查询规格参数
    *@Author：ykym
    * @param: [gid]
    * @return: org.springframework.http.ResponseEntity<java.util.List<com.leyou.item.pojo.SpecParm>>
    * @Date:  17:04 2020/8/24
    */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParms(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "generic",required = false)Boolean generic,
            @RequestParam(value = "searching",required = false)Boolean search
    ){
        List<SpecParam> parms = specificationService.queryParms(gid,cid,generic,search);
        if (CollectionUtils.isEmpty(parms)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(parms);
    }

    /**
    *查询组消息以及组内规格参数，放入组内的集合
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  20:43 2020/9/5
    */
    @GetMapping("{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> list = this.specificationService.querySpecsByCid(cid);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
}
