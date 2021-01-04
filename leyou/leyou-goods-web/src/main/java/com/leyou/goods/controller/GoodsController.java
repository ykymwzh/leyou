package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Author:ykym
 * @Date:2020/9/5 19:42
 */
@Controller
@RequestMapping("item")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsHtmlService goodsHtmlService;
    /**
    *跳转商品详情页
    *@Author：ykym
    * @param: [id, model]
    * @return: java.lang.String
    * @Date:  20:19 2020/9/5
    */
    @GetMapping("{id}.html")
    public String toItemPage(@PathVariable("id")Long id, Model model){
        Map<String,Object> map =  this.goodsService.toItemPage(id);
        //第一次访问，生成静态页面，以后就nginx直接转到根目录的静态页面
        goodsHtmlService.createHtml(id);
        model.addAllAttributes(map);

        return "item";
    }
}
