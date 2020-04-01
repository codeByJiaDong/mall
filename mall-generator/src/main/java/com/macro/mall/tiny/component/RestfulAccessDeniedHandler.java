package com.macro.mall.tiny.component;


import cn.hutool.json.JSONUtil;
import com.macro.mall.common.api.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zjd
 * @description 当访问的接口没有权限时，自定义的返回结果
 * @date 2020/4/1 14:53
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler
{

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        //设置字符集
        response.setCharacterEncoding("UTF-8");
        //设置 返回类型
        response.setContentType("application/json");
        //设置返回的结果
        response.getWriter().println(JSONUtil.parse(CommonResult.forbidden(e.getMessage())));
        //刷新结果
        response.getWriter().flush();
    }
}
