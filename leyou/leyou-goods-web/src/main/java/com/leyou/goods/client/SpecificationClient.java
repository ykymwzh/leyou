package com.leyou.goods.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author:ykym
 * @Date:2020/9/2 16:55
 */
@FeignClient("leyou-item-service")
public interface SpecificationClient extends SpecificationApi {
}
