package com.leyou.user.Controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.awt.font.ShapeGraphicAttribute;

/**
 * @Author:ykym
 * @Date:2020/9/10 17:36
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
    *检验数据是否可用，可用返回true,已经被使用返回true
    *@Author：ykym
    * @param: [data, type]
    * @return: org.springframework.http.ResponseEntity<java.lang.Boolean>
    * @Date:  20:30 2020/9/10
    */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> CheckUserData(@PathVariable("data")String data,@PathVariable("type")Integer type){
        Boolean aBoolean = this.userService.checkData(data,type);
        if (aBoolean == null){
            return ResponseEntity.badRequest().build();
        }
        //正确错误都要返回
        return ResponseEntity.ok(aBoolean);
    }

    /**
    *发送手机验证码
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  20:31 2020/9/11
    */
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone")String phone){
       Boolean aBoolean =  this.userService.sendVerifyCode(phone);
       if (aBoolean == null){
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
    *用户注册，密码md5 salt加密
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  10:12 2020/9/12
    */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code){
        Boolean aBoolean = this.userService.register(user,code);
        if (aBoolean == null || ! aBoolean){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
    *根据用户名和密码查询用户
    *@Author：ykym
    * @param:
    * @return:
    * @Date:  15:33 2020/9/12
    */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username")String username, @RequestParam("password")String password){
       User user =  this.userService.queryUser(username,password);
        if (user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(user);

    }
}

