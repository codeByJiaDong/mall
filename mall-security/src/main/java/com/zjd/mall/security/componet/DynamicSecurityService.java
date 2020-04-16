package com.zjd.mall.security.componet;


import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * @author zjd
 * @Description 动态权限相关的业务类
 * @date 2020/4/16 14:09
 */

public interface DynamicSecurityService
{
    /**
     * 加载资源的ANT 通配符 和 对应资源的Map
     * @return
     */
    Map<String, ConfigAttribute> loadDataSource();
}
