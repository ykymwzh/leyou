package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * 操作spu,sku所有相关的业务
 * @Author:ykym
 * @Date:2020/8/25 15:49
 */
public interface GoodService {
    PageResult<SpuBo> querySpuBoBypage(String key, Boolean saleable, Integer page, Integer rows);
    /**
    *传入spubo对象，实现商品新增，不是品牌新增
    *@Author：ykym
    * @param: [spuBo]
    * @return: void
    * @Date:  10:14 2020/8/27
    */
    void savaGoods(SpuBo spuBo);
    /**
    *根据spubo新增sku,stock,spu,spudetail
    *@Author：ykym
    * @param: [spuId]
    * @return: com.leyou.item.pojo.SpuDetail
    * @Date:  15:47 2020/8/27
    */
    SpuDetail querySpuDetailBySpuId(Long spuId);
    /**
    *根据spuid查询sku以及库存stock信息
    *@Author：ykym
    * @param: [spuId]
    * @return: java.util.List<com.leyou.item.pojo.Sku>
    * @Date:  16:09 2020/8/27
    */
    List<Sku> querySkusBySpuId(Long spuId);
    /**
    *根据spubo修改sku,stock,spu,spudetail
    *@Author：ykym
    * @param: [spuBo]
    * @return: void
    * @Date:  17:00 2020/8/27
    */
    void updateGoods(SpuBo spuBo);

    /**
    *根据spuid查询spu
    *@Author：ykym
    * @param: [id]
    * @return: com.leyou.item.pojo.Spu
    * @Date:  20:26 2020/9/5
    */
    Spu querySpuById(Long id);
    /**
    *根据skuid查询sku
    *@Author：ykym
    * @param: [skuId]
    * @return: com.leyou.item.pojo.Sku
    * @Date:  23:24 2020/9/14
    */
    Sku querySkuById(Long skuId);
}
