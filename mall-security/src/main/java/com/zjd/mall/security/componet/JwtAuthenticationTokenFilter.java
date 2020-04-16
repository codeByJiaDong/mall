package com.zjd.mall.security.componet;


import com.zjd.mall.security.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zjd
 * @Description JWT登录授权过滤器
 * @date 2020/4/16 16:41
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 过滤器的具体操作
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        //获取请求头中的 jwt tokenHead 校验是否正确
        String authHeader = request.getHeader(this.tokenHead);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)){
            //获取token
            String authToken = authHeader.substring(this.tokenHead.length());
            //从token 获取 用户名 标识符
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            log.info("checking username {}", username);
            //获取用户名不能为空 且从 当前线程中  SecurityContextHolder 组件 获取上下文的 认证不能为空
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                //获取用户详情
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)){
                    //赋予权限
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("authenticated user:{}", username);
                    //将权限信息设置到上下文认证中
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);

    }
}
