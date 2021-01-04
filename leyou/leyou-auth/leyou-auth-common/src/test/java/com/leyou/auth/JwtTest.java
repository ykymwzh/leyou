package com.leyou.auth;

import com.leyou.auth.common.JwtUtils;
import com.leyou.auth.common.RsaUtils;
import com.leyou.auth.pojo.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author:ykym
 * @Date:2020/9/13 9:53
 */
public class JwtTest {

    private static final String pubKeyPath = "F:\\ideaproject\\leyou\\leyou-auth\\rsa\\rsa.pub";

    private static final String priKeyPath = "F:\\ideaproject\\leyou\\leyou-auth\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU5OTk2MjU0MH0.bsYMrrkjEVmanXm2ZXmhjI9Naxh4dWQTISGBHwRRy88Nv3s9ACNtq2y6tTxKdVjvJYPJ3_f1FIn_8s5RYA7NhU5pfEhkW5PbcJ5TbuJjyMMf-6xZR_LrZ4QLLei2zZZNzsd-BtBrtqCi7HXwQZb0aAS6icY1W-jwN8uKzSHvEOU";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
