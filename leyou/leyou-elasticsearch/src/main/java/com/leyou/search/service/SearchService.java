package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:ykym
 * @Date:2020/9/2 19:02
 */
@Service
public class SearchService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsRepository goodsRepository;

    //josn转换工具
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
    *创建goods对象
    *@Author：ykym
    * @param: [spu]
    * @return: com.leyou.search.pojo.Goods
    * @Date:  9:14 2020/9/10
    */
    public Goods buildGoods(Spu spu) throws Exception {
        //根据分类id查询分类名称
        List<String> names = categoryClient.queryByNamesById(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
        //根据品牌id查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        //根据spu的id查询sku
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spu.getId());
        //初始化价格，搜集所有价格的集合
        List<Long> prices = new ArrayList<>();
        List<Map<String,Object>> skuMapList  = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            //多张图片以逗号分隔，这里获取第一张
            map.put("image",StringUtils.split(sku.getImages(),","));
            skuMapList.add(map);
        });
        //工具spu的cid3查询规格参数
        List<SpecParam> params = this.specificationClient.queryParms(null, spu.getCid3(), null, true);
        //工具spuid查出spudetail的规格
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());
        //获取通用规格参数并转化josn为map
        Map<Long, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        //获取特殊规格参数
        Map<Long,Object> specialSpecMap= MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {
        });
        //Map接收规格参数
        Map<String,Object>paramMap = new HashMap<>();
        params.forEach(parm->{
            // 判断是否通用规格参数
            if (parm.getGeneric()) {
                // 获取通用规格参数值
                String value = genericSpecMap.get(parm.getId()).toString();
                System.out.println("这里到了吧");
                // 判断是否是数值类型
                if (parm.getNumeric()){
                    System.out.println("啊啊啊");
                    // 如果是数值的话，判断该数值落在那个区间
                    value = chooseSegment(value, parm);
                }
                // 把参数名和值放入结果集中
                paramMap.put(parm.getName(), value);
            } else {
                paramMap.put(parm.getName(), specialSpecMap.get(parm.getId().toString()));
            }
        });


        Goods goods = new Goods();

        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        //分类名称和品牌名称
        goods.setAll(spu.getTitle()+" "+ StringUtils.join(names," ") +" "+brand.getName());
        //所有sku价格
        goods.setPrice(prices);
        //sku转换josn字符串
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        //规格参数
        goods.setSpecs(paramMap);

        return goods;
    }
    /**
    *将数字落在区间里，但是根本进不来，numeric怎么一直是false
    *@Author：ykym
    * @param: [value, p]
    * @return: java.lang.String
    * @Date:  23:54 2020/9/4
    */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            System.out.println(segs);
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        System.out.println(result);
        return result;
    }
    /**
    *商品查询
    *@Author：ykym
    * @param: [searchRequest]
    * @return: com.leyou.common.pojo.PageResult<com.leyou.search.pojo.Goods>
    * @Date:  11:23 2020/9/3
    */
    public SearchResult search(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        if (StringUtils.isEmpty(key)){
            return null;
        }

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        // QueryBuilder basicQuery = QueryBuilders.matchQuery("all", key).operator(Operator.AND); 换成布尔查询
        BoolQueryBuilder basicQuery = buildBoolQueryBuilder(searchRequest);

        builder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));
        builder.withPageable(PageRequest.of(searchRequest.getPage()-1,searchRequest.getSize()));
        builder.withQuery(basicQuery);

        //添加分类和品牌的聚合
        String categoryAggName = "categories";
        String brandAggName = "brands";
        builder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        builder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        //执行查询并强转
        AggregatedPage<Goods> goods = (AggregatedPage<Goods>)this.goodsRepository.search(builder.build());

        //获取并解析聚合
       List<Map<String,Object>> categories =  getCategoryAggResult(goods.getAggregation(categoryAggName));
       List<Brand> brands = getBrandAggResult(goods.getAggregation(brandAggName));
       List<Map<String,Object>> specs = null;

       //一个分类再做聚合
        if ((!CollectionUtils.isEmpty(categories)) && categories.size() == 1){
           specs =  getSpecAggResult((Long)categories.get(0).get("id"),basicQuery);
        }

        return new SearchResult(goods.getTotalElements(),goods.getTotalPages(),goods.getContent(),categories,brands,specs);
    }

    /**
    *构建布尔查询
    *@Author：ykym
    * @param: [searchRequest]
    * @return: org.elasticsearch.index.query.BoolQueryBuilder
    * @Date:  23:58 2020/9/4
    */
    private BoolQueryBuilder buildBoolQueryBuilder(SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder  = new BoolQueryBuilder();
        //添加基本查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all",searchRequest.getKey()).operator(Operator.AND));
        //添加过滤条件
        Map<String, Object> filter = searchRequest.getFilter();
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.equals("品牌",key)){
                key = "brandId";
            }else if (key.equals("分类")){
                key = "cid3";
            }else {
                key = "specs."+key+".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key,entry.getValue()));
        }
       return boolQueryBuilder;
    }

    /**
    *根据查询条件聚合规格参数
    *@Author：ykym
    * @param: [id, basicQuery]
    * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    * @Date:  13:26 2020/9/4
    */
    public List<Map<String, Object>> getSpecAggResult(Long id, QueryBuilder basicQuery) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //添加查询条件
        builder.withQuery(basicQuery);

        //不用普通查询结果集，只要聚合规格c参数
       builder.withSourceFilter(new FetchSourceFilter(new String[]{},null));

       //查询要聚合的规格参数，符合cid并且是searching字段
        List<SpecParam> specParams = this.specificationClient.queryParms(null, id, null, true);

        //添加聚合
        specParams.forEach(specParam -> {
            builder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs." + specParam.getName() + ".keyword"));
        });

        //执行聚合查询
        AggregatedPage<Goods> aggregatedPage = (AggregatedPage<Goods>) this.goodsRepository.search(builder.build());

        //解析聚合结果集,所有聚合转为map，key为规格参数名，value为聚合对象
        List<Map<String,Object>> specs = new ArrayList<>();
        Map<String, Aggregation> aggregationMap = aggregatedPage.getAggregations().asMap();

        for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
            Map<String,Object> map =new HashMap<>();
            map.put("k",entry.getKey());

            //取出每个聚合的桶
            List<String> options = new ArrayList<>();

            StringTerms entryValue = (StringTerms)entry.getValue();

            entryValue.getBuckets().forEach(bucket -> {
                options.add(bucket.getKeyAsString());
            });

            map.put("options",options);
            specs.add(map);
        }

        return specs;
    }

    // private List<Map<String,Object>> getParamAggResult(Long id, QueryBuilder basicQuery) {
    //
    //     // 创建自定义查询构建器
    //     NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    //     // 基于基本的查询条件，聚合规格参数
    //     queryBuilder.withQuery(basicQuery);
    //     // 查询要聚合的规格参数
    //     List<SpecParam> params = this.specificationClient.queryParms(null, id, null, true);
    //     // 添加聚合
    //     params.forEach(param -> {
    //         queryBuilder.addAggregation(AggregationBuilders.terms(param.getName()).field("specs." + param.getName() + ".keyword"));
    //     });
    //     // 只需要聚合结果集，不需要查询结果集
    //     queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
    //
    //     // 执行聚合查询
    //     AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());
    //
    //     // 定义一个集合，收集聚合结果集
    //     List<Map<String, Object>> paramMapList = new ArrayList<>();
    //     // 解析聚合查询的结果集
    //     Map<String, Aggregation> aggregationMap = goodsPage.getAggregations().asMap();
    //     for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
    //         Map<String, Object> map = new HashMap<>();
    //         // 放入规格参数名
    //         map.put("k", entry.getKey());
    //         // 收集规格参数值
    //         List<Object> options = new ArrayList<>();
    //         // 解析每个聚合
    //         StringTerms terms = (StringTerms)entry.getValue();
    //         // 遍历每个聚合中桶，把桶中key放入收集规格参数的集合中
    //         terms.getBuckets().forEach(bucket -> options.add(bucket.getKeyAsString()));
    //         map.put("options", options);
    //         paramMapList.add(map);
    //     }
    //
    //     return paramMapList;
    // }

    /**
    *解析品牌的聚合结果集
    *@Author：ykym
    * @param: [aggregation]
    * @return: java.util.List<com.leyou.item.pojo.Brand>
    * @Date:  21:28 2020/9/3
    */
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;
        //获取桶
        List<Brand> bids = new ArrayList<>();
        //获取品牌id查询品牌
        terms.getBuckets().forEach(bucket -> {
            Brand brand =this.brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
            bids.add(brand);
        });
        return bids;
    }
    /**
    *解析分类结果集
    *@Author：ykym
    * @param: [aggregation]
    * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    * @Date:  21:29 2020/9/3
    */
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;
        return terms.getBuckets().stream().map(bucket -> {
           //初始化map
           Map<String,Object> map =new HashMap<>();
           //获取桶的分类id
            Long id = bucket.getKeyAsNumber().longValue();
            List<String> names = this.categoryClient.queryByNamesById(Arrays.asList(id));
            map.put("id",id);
            map.put("name",names.get(0));
            return map;
        }).collect(Collectors.toList());
    }


    public void save(Long id) throws Exception {
        Spu spu = this.goodsClient.querySpuById(id);
        Goods goods = this.buildGoods(spu);
        this.goodsRepository.save(goods);
    }

    public void delete(Long id) {
        this.goodsRepository.deleteById(id);
    }
}
