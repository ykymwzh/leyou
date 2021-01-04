package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author:ykym
 * @Date:2020/9/13 15:31
 */
@FeignClient("leyou-user-service")
public interface UserClient extends UserApi {
}
