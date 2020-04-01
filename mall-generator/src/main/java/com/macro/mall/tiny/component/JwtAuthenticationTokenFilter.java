package com.macro.mall.tiny.component;


import com.macro.mall.tiny.until.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 一次请求过滤
 * @author zjd
 * @Description
 * @date 2020/4/1 15:57
 */
@Component
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
    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain chain) throws ServletException, IOException {
        //获取校验的请求头
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)){
            //校验体 == 真正拿来校验的token
            String authToken = authHeader.substring(this.tokenHead.length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            log.info("checking username:{}", username);
            //用户名不能为空 && 权限上下文中的 权限信息不能为空
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                //通过用户名查询出用户详情
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                //校验 token 是否过期
                if (jwtTokenUtil.validateToken(authToken,userDetails)){
                    // 创建授权的token
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //赋权？
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //对持有人的上下文赋予 带权限的token
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        chain.doFilter(request,response);
    }
}
