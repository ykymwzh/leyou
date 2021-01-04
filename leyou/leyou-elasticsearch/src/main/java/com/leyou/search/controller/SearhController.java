package com.leyou.search.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author:ykym
 * @Date:2020/9/3 10:54
 */
@Controller
public class SearhController {
    @Autowired
    private SearchService searchService;
    /**
    *根据searchrequest搜索商品
    *@Author：ykym
    * @param: [searchRequest]
    * @return: org.springframework.http.ResponseEntity<com.leyou.common.pojo.PageResult<com.leyou.search.pojo.Goods>>
    * @Date:  11:12 2020/9/3
    */
    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest searchRequest){
     SearchResult result = this.searchService.search(searchRequest);
     if (result == null){
         return ResponseEntity.notFound().build();
     }
     return ResponseEntity.ok(result);
    }
}
