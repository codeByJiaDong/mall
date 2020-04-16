package com.zjd.mall.security.componet;


import cn.hutool.json.JSONUtil;
import com.zjd.mall.common.api.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zjd
 * @Description 自定义返回结果：没有权限访问时的处理器
 * @date 2020/4/16 16:25
 */

public class RestfulAccessDeniedHandler implements AccessDeniedHandler
{

    /**
     * 处理器执行的内容
     * @param request
     * @param response
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e)
            throws IOException, ServletException {
        //设置这个请求头允许跨域
        response.setHeader("Access-Control-Allow-Origin","*");
        //不允许页面缓存，否则可能出现 无请求
        response.setHeader("Cache-Control" , "no-cache");
        //编码 utf-8
        response.setCharacterEncoding("UTF-8");
        //设置响应体格式 json
        response.setContentType("application/json");
        //设置响应提内容
        response.getWriter().println(JSONUtil.parse(CommonResult.forbidden(e.getMessage())));
        //页面刷新
        response.getWriter().flush();
    }
}
