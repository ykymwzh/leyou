package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/25 15:49
 */
@Controller
public class GoodsController {
    @Autowired
    private GoodService goodService;
    /**
    *分页查询spu
    *@Author：ykym
    * @param: [key, saleable, page, rows]
    * @return: org.springframework.http.ResponseEntity<com.leyou.common.pojo.PageResult<com.leyou.item.bo.SpuBo>>
    * @Date:  15:45 2020/9/2
    */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuBoByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable" ,required = false)Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows
            ){
            PageResult<SpuBo> result  = goodService.querySpuBoBypage(key,saleable,page,rows);

            if (CollectionUtils.isEmpty(result.getItems())){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
    }

    /**
    *根据spubo新增sku,spu,stock,spudetail等表
    *@Author：ykym
    * @param: [spuBo]
    * @return: org.springframework.http.ResponseEntity<java.lang.Void>
    * @Date:  15:44 2020/8/27
    */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        this.goodService.savaGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
    *根据spubo修改商品信息，提交方式为put
    *@Author：ykym
    * @param: [spuBo]
    * @return: org.springframework.http.ResponseEntity<java.lang.Void>
    * @Date:  17:00 2020/8/27
    */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        this.goodService.updateGoods(spuBo);
        return ResponseEntity.noContent().build();
    }

    /**
    *根据spuid查询spudetail
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  15:44 2020/8/27
    */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
      SpuDetail spuDetail =  this.goodService.querySpuDetailBySpuId(spuId);
      if (spuDetail == null){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      return ResponseEntity.ok(spuDetail);
    }

    /**
    *根据spuid查询sku以及库存信息
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  16:00 2020/8/27
    */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam("id")Long spuId){
       List<Sku> skus =  this.goodService.querySkusBySpuId(spuId);
       if (CollectionUtils.isEmpty(skus)){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(skus);
    }

    /**
    *根据spuid查询spu
    *@Author：ykym
    * @param: [id]
    * @return: org.springframework.http.ResponseEntity<com.leyou.item.pojo.Spu>
    * @Date:  20:33 2020/9/5
    */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id")Long id){
        Spu spu = this.goodService.querySpuById(id);
        if (spu == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }

    /**
    *根据skuid查询sku
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  23:20 2020/9/14
    */
    @GetMapping("sku/{skuId}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("skuId")Long skuId){
       Sku sku =  this.goodService.querySkuById(skuId);
       if (sku == null){
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
       return ResponseEntity.ok(sku);
    }
}
