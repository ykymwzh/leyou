package com.leyou.cart.config;

import com.leyou.cart.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author:ykym
 * @Date:2020/9/14 21:02
 */
@Configuration
public class LeyouWebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private LoginFilter loginFilter;

    /**
    *登记拦截器
    *@Author：ykym
    * @param: [registry]
    * @return: void
    * @Date:  21:03 2020/9/14
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注入拦截器拦截所有路径
        registry.addInterceptor(loginFilter).addPathPatterns("/**");
    }
}
