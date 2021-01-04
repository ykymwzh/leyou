package com.leyou.search.pojo;

import ch.qos.logback.core.joran.util.StringToObjectConverter;

import java.util.Map;

/**
 * @Author:ykym
 * @Date:2020/9/3 10:47
 */
public class SearchRequest {
    //搜索条件
    private String key;
    //当前页
    private Integer page;
    //接收过滤消息
    private Map<String,Object> filter;

    //每页大小，固定
    private static final Integer DEFAULT_SIZE=20;
    //默认页
    private static  final Integer DEFAULT_PAGE=1;

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page == null){
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize(){
        return DEFAULT_SIZE;
    }
}
