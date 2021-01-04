package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @Author:ykym
 * @Date:2020/9/2 16:53
 */
@FeignClient("leyou-item-service")
public interface BrandClient extends BrandApi {
}
