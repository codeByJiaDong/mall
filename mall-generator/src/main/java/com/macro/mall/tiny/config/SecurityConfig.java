package com.macro.mall.tiny.config;


import com.macro.mall.tiny.component.JwtAuthenticationTokenFilter;
import com.macro.mall.tiny.component.RestAuthenticationEntryPoint;
import com.macro.mall.tiny.component.RestfulAccessDeniedHandler;
import com.macro.mall.tiny.dto.AdminUserDetails;
import com.macro.mall.tiny.mbg.model.UmsAdmin;
import com.macro.mall.tiny.mbg.model.UmsPermission;
import com.macro.mall.tiny.service.UmsAdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zjd
 * @Description SpringSecurity 的配置
 * @date 2020/4/1 9:30
 */
@Configuration
// 自动配置 Security
@EnableWebSecurity
//在方法前 判断 权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Resource
    private UmsAdminService umsAdminService;
    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //天线 适配器
                .antMatchers(           //允许对于网站静态资源的无授权访问
                        HttpMethod.GET
                        ,"/"
                        ,"*.html"
                        ,"/favicon.ico"
                        ,"/**/*.css"
                        ,"**/*.js"
                        ,"swagger-resource/**"
                        ,"/v2/api-docs/**"
                )
                .permitAll()
                .antMatchers(
                        "/admin/login"
                        ,"admin/register") //对登录注册允许匿名访问
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS) //跨域请求前，先发起一次预请求
                .permitAll()
                .antMatchers("/**") // 测试时候允许所有请求都可以访问
                .permitAll()
                .anyRequest() //除上面外的所有请求全部需要鉴权认证
                .authenticated();
        //禁用缓存
        httpSecurity.headers().cacheControl();
        //添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        //异常处理器
        httpSecurity.exceptionHandling()
                //未登录处理
                .accessDeniedHandler(restfulAccessDeniedHandler)
                // token 超时处理
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()) //用户的信息对象
            .passwordEncoder(passwordEncoder()); //密码加密
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 获取用户信息
     * @return
     */
    @Override
    protected UserDetailsService userDetailsService() {
        //获取用户信息
        return username -> {
            UmsAdmin admin = umsAdminService.getAdminByUsername(username);
            //用户不为空则获取用户权限，授权
            if (admin != null){
                List<UmsPermission> permissionList =
                        umsAdminService.getPermissionList(admin.getId());
                //返回的是 我们的实现
                return new AdminUserDetails(admin,permissionList);
            }
            throw new UsernameNotFoundException("用户名和密码错误");
        };
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
