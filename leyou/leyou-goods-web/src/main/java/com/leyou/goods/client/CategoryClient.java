package com.leyou.goods.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author:ykym
 * @Date:2020/9/2 16:54
 */
@FeignClient("leyou-item-service")
public interface CategoryClient extends CategoryApi {
}
