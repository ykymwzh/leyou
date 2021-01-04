package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/12 14:52
 */
@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
    *由父类目查询子类目
    *@Author：ykym
    * @param: [pid]
    * @return: org.springframework.http.ResponseEntity<java.util.List<com.leyou.item.pojo.Category>>
    * @Date:  23:54 2020/8/13    
    */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value = "pid" ,defaultValue = "0") Long pid){
        if (pid == null || pid < 0 ){
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories  = this.categoryService.queryCategoriesByPid(pid);
        if (CollectionUtils.isEmpty(categories)){
            /**404响应*/
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(categories);
    }

    /**
    *通过id查询品牌
    *@Author：ykym
    * @param: [bid]
    * @return: org.springframework.http.ResponseEntity<java.util.List<com.leyou.item.pojo.Category>>
    * @Date:  17:03 2020/8/24
    */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid")Long bid){
        List<Category> categories = this.categoryService.queryByBrandId(bid);
        if (categories.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categories);
    }

    /**
    *根据cid查询名称
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  15:52 2020/9/2
    */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryByNamesById(@RequestParam List<Long> ids){
        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }

}
