package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author:ykym
 * @Date:2020/8/14 11:24
 */
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    /**
    *根据查询条件分页并排序，不输入默认全部查询
    *@Author：ykym
    * @param: [key, page, sortBy, desc]
    * @return: org.springframework.http.ResponseEntity<com.leyou.common.pojo.PageResult<com.leyou.item.pojo.Brand>>
    * @Date:  12:09 2020/8/14
    */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc
    ){
       PageResult<Brand> pageResult  = this.brandService.queryBrandsByPage(key,page,rows,sortBy,desc);
       if (CollectionUtils.isEmpty(pageResult.getItems())){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(pageResult);
    }

    /**
    *新增品牌
    *@Author：ykym
    * @param: [brand, cids]
    * @return: org.springframework.http.ResponseEntity<java.lang.Void>
    * @Date:  20:50 2020/8/16
    */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand,@RequestParam("cids") List<Long>cids){
        this.brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
    *删除品牌
    *@Author：ykym
    * @param: [bid]
    * @return: org.springframework.http.ResponseEntity<java.util.List<com.leyou.item.pojo.Category>>
    * @Date:  12:23 2020/8/20
    */
    @GetMapping("did/{did}")
    public ResponseEntity<Void> deleteByBrandId(@PathVariable("did")Long did){
        this.brandService.deleteByBrandId(did);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
    *根据分类cid查询品牌
    *@Author：ykym
    * @param: []
    * @return: org.springframework.http.ResponseEntity<java.util.List<com.leyou.item.pojo.Brand>>
    * @Date:  0:04 2020/8/26
    */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid")Long cid){
        List<Brand> brands = this.brandService.queryBrandsByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(brands);
    }
    /**
    *根据id查询品牌
    *@Author：ykym
    * @param: [id]
    * @return: org.springframework.http.ResponseEntity<com.leyou.item.pojo.Brand>
    * @Date:  17:12 2020/9/6
    */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        Brand brand = this.brandService.queryBrandById(id);
        if (brand == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }
}
