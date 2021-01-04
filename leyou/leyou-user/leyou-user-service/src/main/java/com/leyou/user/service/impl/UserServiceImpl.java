package com.leyou.user.service.impl;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.service.UserService;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author:ykym
 * @Date:2020/9/10 17:36
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    static final String KEY_PREFIX = "user:code:phone";

    @Override
    public Boolean checkData(String data, Integer type) {
        User user = new User();
       switch (type){
           case 1:
               user.setUsername(data);
               break;
           case 2:
               user.setPhone(data);
               break;
           default:
               return null;
       }
        return this.userMapper.selectCount(user) == 0;
    }

    @Override
    public Boolean sendVerifyCode(String phone) {
        //获取验证码
        String code = NumberUtils.generateCode(6);
        try {
            //发送短信
            Map<String,String>msg = new HashMap<>();
            msg.put("phone",phone);
            msg.put("code",code);
            //通知sms模块
            amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE","sms.verify.code",msg);
            //将信息存入redis
            redisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            LOGGER.error("发送短信失败，phone:{},code:{}",phone,code);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean register(User user, String code) {
        //校验验证码
        String outcode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (! StringUtils.equals(code,outcode)){
            return false;
        }
        //生成salt
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //md5加密
        String newpassword = CodecUtils.md5Hex(user.getPassword(), salt);
        user.setPassword(newpassword);
        //补齐user参数
        user.setId(null);
        user.setCreated(new Date());
        ///添加的数据库
       boolean b = this.userMapper.insertSelective(user) == 1;
        //删除redis记录
        if (b) {
            this.redisTemplate.delete(KEY_PREFIX + user.getPhone());
        }
        return b;
    }

    @Override
    public User queryUser(String username, String password) {
        //判断非空
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return null;
        }

        //根据用户名查询user
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (user == null){
            return null;
        }

        //判断数据库的密码和输入的密码是否相同，解密
        String salt = user.getSalt();
        String md5Hex = CodecUtils.md5Hex(password, salt);
        if ( ! md5Hex.equals(user.getPassword())){
            return null;
        }

        return user;
    }
}
