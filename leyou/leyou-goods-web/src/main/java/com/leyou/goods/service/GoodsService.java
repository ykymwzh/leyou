package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author:ykym
 * @Date:2020/9/6 16:27
 */
@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;


    public Map<String, Object> toItemPage(Long id) {
        //查询spu
        Spu spu = this.goodsClient.querySpuById(id);
        //查询spudetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(id);
        //查询skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(id);
        //查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        //查询分类
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryByNamesById(cids);
        List<Map<String,Object>> categories = new ArrayList<>();
        for (int i = 0; i <cids.size() ; i++) {
            Map<String,Object> objectMap = new HashMap<>();
            objectMap.put("id",cids.get(i));
            objectMap.put("name",names.get(i));
            categories.add(objectMap);
        }
        //查询规格参数组
        List<SpecGroup> groups = this.specificationClient.querySpecsByCid(spu.getCid3());
        //查询特有规格参数名称
        List<SpecParam> specParams = this.specificationClient.queryParms(null, spu.getCid3(), null, false);
        Map<Long,String> params = new HashMap<>();
        for (SpecParam param : specParams) {
            params.put(param.getId(),param.getName());
        }

        Map<String,Object> map = new HashMap();
        //存入spu
        map.put("spu",spu);
       //存入spudetail
        map.put("spuDetail",spuDetail);
       //存入skus
        map.put("skus",skus);
       //品牌
        map.put("brand",brand);
       //存入商品分类的id和名字
        map.put("categories",categories);
       //存入规格组
        map.put("groups",groups);
       //存入特有规格参数名称
        map.put("params",params);

        return map;
    }
}
