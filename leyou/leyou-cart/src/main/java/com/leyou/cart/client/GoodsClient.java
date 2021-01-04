package com.leyou.cart.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author:ykym
 * @Date:2020/9/14 23:26
 */
@FeignClient("leyou-item-service")
public interface GoodsClient extends GoodsApi {
}
