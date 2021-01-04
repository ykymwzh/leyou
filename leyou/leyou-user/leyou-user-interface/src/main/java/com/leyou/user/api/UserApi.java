package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author:ykym
 * @Date:2020/9/13 15:29
 */

public interface UserApi {
    /**
     *根据用户名和密码查询用户
     *@Author：ykym
     * @param:
     * @return:
     * @Date:  15:33 2020/9/12
     */
    @GetMapping("query")
    public User queryUser(@RequestParam(value = "username")String username, @RequestParam( value = "password")String password);
}
