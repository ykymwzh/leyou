package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author:ykym
 * @Date:2020/9/2 19:07
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
