package com.macro.mall.tiny.until;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
     */
    private String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                //加密的token
                .setClaims(claims)
                //设置过期时间
                .setExpiration(generateExpirationDate())
                //设置过期算法 和 盐
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token 中 解析 JWT 的 负载
     * @param token
     * @return
     */
    private Claims getClaimsFromToekn(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("JWT格式验证失败：{}",token);
        }
        return claims;
    }

    /**
     * 生成token的過期時間
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis()+ expiration * 1000);
    }

    /**
     * 通过token 获取过期时间
     * @param token
     * @return
     */
    public Date getExpiredDateFromToken(String token){
        Claims claims = getClaimsFromToekn(token);
        // 返回截止日期
        return claims.getExpiration();
    }

    /**
     * 从token中获取登录用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToekn(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token 是否还有效
     * @param token 客户端传入的token
     * @param userDetails 从数据库中查询出来的用户数据
     *                    校验条件 = 从token 取出来的用户名 要和 数据库取出的是相同的
     *                              && token 没有过期
     * @return
     */
    public boolean validateToken(String token ,UserDetails userDetails){
        String username = getUserNameFromToken(token);
        Boolean result = username.equals(userDetails.getUsername())&& ! isTokenExpired(token);
        return result;
    }

    /**
     * 判断token 是否已经失效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        // 获取过期时间
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 判断token 是否可以被刷新
     * @param token
     * @return
     */
    private boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    /**
     * 刷新 token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToekn(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }






}
