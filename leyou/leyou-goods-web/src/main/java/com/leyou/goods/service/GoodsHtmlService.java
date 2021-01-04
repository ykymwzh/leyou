package com.leyou.goods.service;

import com.leyou.goods.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @Author:ykym
 * @Date:2020/9/8 11:39
 */
@Service
public class GoodsHtmlService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TemplateEngine engine;
    @Autowired
    private ThreadUtils threadUtils;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlService.class);

    /**
    *创建静态页面
    *@Author：ykym
    * @param: [spuid]
    * @return: void
    * @Date:  11:41 2020/9/8
    */
    public void createHtml(Long spuid){
        PrintWriter writer = null;
        try {
        //获取页面数据
        Map<String,Object> map = this.goodsService.toItemPage(spuid);
        //创建上下文对象
        Context context = new Context();
        //放入数据，搭建数据模型
        context.setVariables(map);

        //创建输出流
        File file = new File("D:\\nginx\\nginx-1.14.0\\html\\item\\"+spuid+".html");
        writer = new PrintWriter(file);
        //执行页面静态化
        this.engine.process("item",context,writer);
        } catch (Exception e) {
            LOGGER.error("页面静态化错误"+e,spuid);
        }finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public void asyncExcute(Long spuId) {
        ThreadUtils.excute(() -> createHtml(spuId));
    }
}
