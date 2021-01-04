package com.leyou.auth.controller;

import com.leyou.auth.common.JwtUtils;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.geom.RectangularShape;
import java.awt.image.RescaleOp;

/**
 * @Author:ykym
 * @Date:2020/9/13 11:12
 */

@EnableConfigurationProperties(JwtProperties.class)
@Controller
public class AuthController {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private AuthService authService;

    /**
    *登入授权
    *@Author：ykym
    * @param: []
    * @return: org.springframework.http.ResponseEntity<java.lang.Void>
    * @Date:  11:16 2020/9/13
    */
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        //登入校验
       String token = this.authService.accredit(username,password);
       if (StringUtils.isBlank(token)){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       }
       //存入cookie,httponly为true，防止js获得
        CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getExpire()*60,null,true);
        return ResponseEntity.ok().build();
    }

    /**
    *校验用户信息
     * 从cookie取出token,用公钥解密
     * 刷新有效时间
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  19:44 2020/9/13
    */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("LY_TOKEN")String token,
                                               HttpServletRequest request,
                                               HttpServletResponse response

    ){

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            if (userInfo == null){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            //jwt刷新有效时间,这里时间单位为分
            String newtoken = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            //刷新cookie，时间单位为秒
            CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),newtoken,jwtProperties.getExpire()*60);

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //出现异常报错
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
