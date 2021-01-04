package com.leyou.user.service;

import com.leyou.user.pojo.User;

/**
 * @Author:ykym
 * @Date:2020/9/10 17:36
 */
public interface UserService {
    /**
    *检验数据是否可用，可用返回true,已经被使用返回true
    *@Author：ykym
    * @param: [data, type]
    * @return: java.lang.Boolean
    * @Date:  20:30 2020/9/10
    */
    Boolean checkData(String data, Integer type);
    /**
    *发送验证码短信
    *@Author：ykym
    * @param: [phone]
    * @return: java.lang.Boolean
    * @Date:  20:36 2020/9/11
    */
    Boolean sendVerifyCode(String phone);

    /**
    *用户注册，md5和salt加密
    *@Author：ykym
    * @param: [user, code]
    * @return: java.lang.Boolean
    * @Date:  10:16 2020/9/12
    */
    Boolean register(User user, String code);
    /**
    *根据用户名和密码查询用户信息
    *@Author：ykym
    * @param: [username, password]
    * @return: com.leyou.user.pojo.User
    * @Date:  15:36 2020/9/12
    */
    User queryUser(String username, String password);
}
