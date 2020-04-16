package com.zjd.mall.security.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjd
 * @Description 用于配置不需要保护的资源路径
 * @date 2020/4/16 15:46
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "secure.ignored")
public class IgnoreUrlConfig
{

    private List<String> urls = new ArrayList<>();
}
