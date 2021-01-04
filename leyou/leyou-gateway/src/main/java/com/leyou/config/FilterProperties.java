package com.leyou.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/9/13 20:30
 */
@ConfigurationProperties("leyou.filter")
public class FilterProperties {

    private List<String> allowPaths ;

    public List<String> getAllowPaths() {
        return allowPaths;
    }

    public void setAllowPaths(List<String> allowPaths) {
        this.allowPaths = allowPaths;
    }
}
