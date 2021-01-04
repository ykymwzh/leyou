package com.leyou.auth.service;

/**
 * @Author:ykym
 * @Date:2020/9/13 11:12
 */
public interface AuthService {
    /**
    *登入校验
    *@Author：ykym
    * @param: [username, password]
    * @return: java.lang.String
    * @Date:  11:19 2020/9/13
    */
    String accredit(String username, String password);
}
