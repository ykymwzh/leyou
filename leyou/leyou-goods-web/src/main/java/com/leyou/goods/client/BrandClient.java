package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author:ykym
 * @Date:2020/9/6 17:08
 */
@FeignClient("leyou-item-service")
public interface BrandClient extends BrandApi{
}
