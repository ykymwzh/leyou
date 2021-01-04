package com.leyou.common.pojo;

import java.util.List;

/**
 * @param <T>
 */
public class PageResult<T>{
    //总条数
    private Long total;
    //总页数
    private Integer totalPages;
    //当前页数据
    private List<T> items;

    public PageResult(){

    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total,Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPages = totalPage;
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
