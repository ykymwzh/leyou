package com.leyou.cart.filter;

import com.leyou.auth.common.JwtUtils;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.utils.CookieUtils;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:ykym
 * @Date:2020/9/14 20:53
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginFilter extends HandlerInterceptorAdapter {
    private static final  ThreadLocal<UserInfo> THREAD_LOCAL  = new ThreadLocal<>();
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie的Token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        //解析token，获取用户信息
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        if (userInfo == null){
            return false;
        }
        THREAD_LOCAL.set(userInfo);
        return true;
    }

    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //使用tomcat线程池，线程永远不会结束，那样就不会自动释放threadlocal资源
        THREAD_LOCAL.remove();
    }
}
