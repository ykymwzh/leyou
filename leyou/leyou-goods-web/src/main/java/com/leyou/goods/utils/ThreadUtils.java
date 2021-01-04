package com.leyou.goods.utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程工具类
 * @Author:ykym
 * @Date:2020/9/8 13:44
 */
@Component
public class ThreadUtils {
    private static final ExecutorService es = Executors.newFixedThreadPool(10);

    public static void  excute(Runnable runnable){
        es.submit(runnable);
    }


}
