package com.leyou.cart.service.impl;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.filter.LoginFilter;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:ykym
 * @Date:2020/9/14 23:14
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "leyou:cart:uid";

    static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);


    @Override
    public void addCart(Cart cart) {
        //获取登入用户
        UserInfo userInfo = LoginFilter.getUserInfo();
        //redis的key
        String key = KEY_PREFIX+userInfo.getId();

        //获取hash操作对象,map<string,map<object,object>>,前面为userId,后面的键为skuId
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);

        //查询是否存在
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean aBoolean = ops.hasKey(key);
        if (aBoolean){
            //value里面存的是json类型的数据
            String json = ops.get(skuId.toString()).toString();
            cart = JsonUtils.parse(json,Cart.class);
            cart.setNum(cart.getNum()+num);
        }else {
            //不存在，新增购物车数据
            cart.setUserId(userInfo.getId());
            //其他商品信息
            Sku sku = this.goodsClient.querySkuById(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
        }
        ops.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    @Override
    public List<Cart> queryCartList() {
        //获取登入用户
        UserInfo userInfo = LoginFilter.getUserInfo();
        String key = KEY_PREFIX+userInfo.getId();
        //判断是否有购物车
        if (! this.redisTemplate.hasKey(key)){
            return null;
        }
        //获取购物车列表
        BoundHashOperations<String, Object, Object> ops = this.redisTemplate.boundHashOps(key);
        //就是最后一个object的值
        List<Object> carts = ops.values();
        //判断是否有数据
        if (carts == null){
            return null;
        }

        //返回购物车数据
        return carts.stream().map(o->JsonUtils.parse(o.toString(),Cart.class)).collect(Collectors.toList());


    }

    @Override
    public void updateNum(Cart cart) {
        //获取登入信息
        UserInfo userInfo = LoginFilter.getUserInfo();

        //获取hash操作对象
        BoundHashOperations<String, Object, Object> ops = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        //获取购物车信息
        Integer num = cart.getNum();
        Long skuId = cart.getSkuId();

        //更新数量
        String json = ops.get(skuId.toString()).toString();
        cart = JsonUtils.parse(json, Cart.class);

        //写入购物车
        cart.setNum(num);
        ops.put(skuId.toString(),JsonUtils.serialize(cart));
    }

    @Override
    public void deleteCart(String skuId) {
        UserInfo userInfo = LoginFilter.getUserInfo();
        BoundHashOperations<String, Object, Object> ops = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        ops.delete(skuId);
    }
}
