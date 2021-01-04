package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author:ykym
 * @Date:2020/9/10 0:27
 */
@Component
public class GoodsListener {
    @Autowired
    private SearchService searchService;
    /**
    *执行新增和删除的同步
    *@Author：ykym
    * @param: [id]
    * @return: void
    * @Date:  9:25 2020/9/10
    */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.SEARCH.SAVE.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.update","item.save"}
    ))
    public void save(Long id) throws Exception {
        if (id == null){return;}
        this.searchService.save(id);
    }

    /**
    *执行删除的同步
    *@Author：ykym
    * @param: [id]
    * @return: void
    * @Date:  9:25 2020/9/10
    */
    @RabbitListener(bindings = @QueueBinding(
            value =@Queue(value = "LEYOU.SEARCH.DELETE.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void delete(Long id)throws Exception{
        if (id == null){return;}
        this.searchService.delete(id);
    }
}
