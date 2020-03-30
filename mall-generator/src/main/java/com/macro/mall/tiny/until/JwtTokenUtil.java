package com.macro.mall.tiny.until;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *
 * @author zjd
 * @Description  用于生成和解析JWT token的工具类
 * @date 2020/3/30 16:26
 */
@Component
@Slf4j
public class JwtTokenUtil
{

    /**
     * generateToken(UserDetails userDetails) :用于根据登录用户信息生成token
     * getUserNameFromToken(String token)：从token中获取登录用户的信息
     * validateToken(String token, UserDetails userDetails)：判断token是否还有效
     */

    /**
     * 声明 用户名
     */
    private static final String CLAIM_KEY_USERNAME= "sub";
    /**
     * 声明 创建
     */
    private static final String CLAIM_KEY_CREATED = "created";

    @Value(value = "${jwt.secret}")
    private String secret;

    @Value(value = "${jwt.expiration}")
    private Long expiration;

 /*   *//**
     * 根据负责生成JWT的token
     * @param claims
     * @return
     *//*
    private String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
    }*/

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis()+ expiration * 1000);
    }
}
