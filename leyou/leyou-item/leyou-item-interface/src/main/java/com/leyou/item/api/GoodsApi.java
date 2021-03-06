package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/25 15:49
 */
public interface GoodsApi {
    /**
    *分页查询spu
    *@Author：ykym
    * @param: [key, saleable, page, rows]
    * @return: org.springframework.http.ResponseEntity<com.leyou.common.pojo.PageResult<com.leyou.item.bo.SpuBo>>
    * @Date:  15:45 2020/9/2
    */
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuBoByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable" ,required = false)Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows
            );

    /**
    *根据spuid查询spudetail
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  15:44 2020/8/27
    */
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId")Long spuId);

    /**
    *根据spuid查询sku以及库存信息
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  16:00 2020/8/27
    */
    @GetMapping("sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam("id")Long spuId);

    /**
     *根据spuid查询spu
     *@Author：ykym
     * @param: [id]
     * @return: org.springframework.http.ResponseEntity<com.leyou.item.pojo.Spu>
     * @Date:  20:33 2020/9/5
     */
    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id")Long id);

    /**
     *根据skuid查询sku
     *@Author：ykym
     * @param:
     * @return:
     * @Date:  23:20 2020/9/14
     */
    @GetMapping("sku/{skuId}")
    public Sku querySkuById(@PathVariable("skuId")Long skuId);
}
