package com.macro.mall.tiny.component;


import cn.hutool.json.JSONUtil;
import com.macro.mall.common.api.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zjd
 * @Description 当未登录或token失效访问接口时，自定义的返回结果
 * @date 2020/4/1 15:01
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint
{

    @Override
    public void commence(HttpServletRequest request
            , HttpServletResponse response
            , AuthenticationException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(CommonResult.unauthorized(e.getMessage())));
        response.getWriter().flush();
    }
}
