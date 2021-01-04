package com.leyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author:ykym
 * @Date:2020/8/13 17:49
 */
@Configuration
public class LeyouCorsConfig {
    /**
     *解决跨域问题
     *@Author：ykym
     * @param: []
     * @return: org.springframework.web.filter.CorsFilter
     * @Date:  17:56 2020/8/13
     */
    @Bean
    public CorsFilter corsFilter(){
        /**添加cors配置信息*/
        CorsConfiguration configuration = new CorsConfiguration();
        //允许的域,写*的话无法使用cookies
        configuration.addAllowedOrigin("http://manage.leyou.com");
        //允许cookies
        configuration.setAllowCredentials(true);
        //允许所有请求方式
        configuration.addAllowedMethod("*");
        //允许所有头信息
        configuration.addAllowedHeader("*");

        //配置cors源信息，配置映射路径，请求那些路径时会生效
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        //返回新的监听器
        return new CorsFilter(source);
    }
}
