package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/9/14 23:13
 */
@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    /**
    *新增购物车
    *@Author：ykym
    * @param: [cart]
    * @return: org.springframework.http.ResponseEntity<java.lang.Void>
    * @Date:  0:30 2020/9/15
    */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }

    /**
    *查询购物车列表，redis中的购物车信息，给登入状态下的购物车页面
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  0:33 2020/9/15
    */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList(){
        //返回购物车列表信息
       List<Cart> carts =  this.cartService.queryCartList();
       if (carts == null){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(carts);
    }

    /**
    *修改购物车商品数量
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  10:03 2020/9/15
    */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart){
        this.cartService.updateNum(cart);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
    *删除一条购物车记录
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  10:26 2020/9/15
    */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId")String skuId){
        this.cartService.deleteCart(skuId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
