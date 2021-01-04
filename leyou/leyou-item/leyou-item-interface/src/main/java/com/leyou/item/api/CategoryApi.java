package com.leyou.item.api;

import com.leyou.item.pojo.Category;
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
 * @Date:2020/8/12 14:52
 */
@RequestMapping("category")
public interface CategoryApi {

    /**
    *根据cid查询名称
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  15:52 2020/9/2
    */
    @GetMapping("names")
    public List<String> queryByNamesById(@RequestParam List<Long> ids);

}
