package com.zjd.mall.security.config;


import com.zjd.mall.security.componet.DynamicSecurityService;
import com.zjd.mall.security.componet.JwtAuthenticationTokenFilter;
import com.zjd.mall.security.componet.RestAuthenticationEntryPoint;
import com.zjd.mall.security.componet.RestfulAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.List;

/**
 * @author zjd
 * @Description 对springSecurity 的配置扩展，支持自定义白名单资源路径和查询用户逻辑
 * @date 2020/4/16 14:07
 */
@Configuration
//自动配置 Security 默认配置
@EnableWebSecurity
//WebSecurityConfigurerAdapter的类上加
//@EnableGlobalMethodSecurity注解， 来判断用户对某个控制层的方法是否具有访问权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    /**
     *  动态权限相关业务类
     */
    @Resource
    private DynamicSecurityService dynamicSecurityService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //拦截请求，然后授权
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>
                .ExpressionInterceptUrlRegistry registry
                = httpSecurity.authorizeRequests();
        //对不需要保护的资源路径 放行
        List<String> urls = ignoreUrlsConfig().getUrls();
        urls.forEach(url->registry
                .antMatchers(url)
                .permitAll());
        //对跨域请求的第一次握手 OPTION
        registry.antMatchers(HttpMethod.OPTIONS)
                .permitAll();
        //除了上面那些放行的，任何请求需要身份认证
        registry.and()
                .authorizeRequests()
                .anyRequest()
                .authenticated() //已认证
                //session 不使用就关了 ，跨站请求防护关了
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //自定义权限拒绝类，要不抛异常了
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler()) //异常处理器
                .authenticationEntryPoint(restAuthenticationEntryPoint()) //异常切面
                //使用jwt 认证需要 jwt 过滤器进行认证呀
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class);
        if (dynamicSecurityService!=null){
            registry.and().addFilterBefore(jwtAuthenticationTokenFilter(),
                    FilterSecurityInterceptor.class);
        }
    }

    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    private RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    private RestfulAccessDeniedHandler restfulAccessDeniedHandler() {
        return new RestfulAccessDeniedHandler();
    }

    @Bean
    public IgnoreUrlConfig ignoreUrlsConfig() {
        return new IgnoreUrlConfig();
    }
}
