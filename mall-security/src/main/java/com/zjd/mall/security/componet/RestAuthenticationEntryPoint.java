package com.zjd.mall.security.componet;


import cn.hutool.json.JSONUtil;
import com.zjd.mall.common.api.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zjd
 * @Description 自定义返回结果：未登录或登录过期的
 * @date 2020/4/16 16:33
 */

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 感觉跟处理器一样的代码，妈的
     *
     * @param request
     * @param response
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e)
            throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(CommonResult.unauthorized(e.getMessage())));
        response.getWriter().flush();
    }
}
