package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/9/14 23:14
 */
public interface CartService {
    /**
    *新增购物车
    *@Author：ykym
    * @param: [cart]
    * @return: void
    * @Date:  0:31 2020/9/15
    */
    void addCart(Cart cart);

    /**
    *查询redis中的购物车信息
    *@Author：ykym
    * @param: []
    * @return: java.util.List<com.leyou.cart.pojo.Cart>
    * @Date:  0:36 2020/9/15
    */
    List<Cart> queryCartList();

    /**
    *修改购物车商品数量
    *@Author：ykym
    * @param: [cart]
    * @return: void
    * @Date:  10:10 2020/9/15
    */
    void updateNum(Cart cart);

    void deleteCart(String skuId);
}
