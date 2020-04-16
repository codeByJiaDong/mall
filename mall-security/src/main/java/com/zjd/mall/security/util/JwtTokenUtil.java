package com.zjd.mall.security.util;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zjd
 * @Description jwt token 生成的工具类
 * header 的格式 （算法、token 的类型）
 * {"alg":"HS512", "typ":"JWT"}
 * payload的格式（用户名、创建时间，生成时间）
 * {"sub":"wang", "created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * @date 2020/4/15 11:07
 */
@Slf4j
public class JwtTokenUtil
{
    /**
     * 令牌名
     */
    private static final String CLAIM_KEY_USERNAME = "sub";
    /**
     * 创建者
     */
    private static final String CLAIM_KEY_CREATED = "created";
    private static final int REFRESHSECOND = 30 * 60;

    /**
     *  秘密 也是盐
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * 失效截止时间
     */
    @Value("${jwt.expiration}")
    private Long expiration;
    /**
     * token 前缀 也是 token Head 存放的是 加密算法 和 类型
     */
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 负责 JWT 创建一个令牌 token
     * @param claims 要求
     * @return token
     */
    private String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims) //负载 就是 你加密的信息内容
                .setExpiration(generateExpirationDate()) // 过期的时间
                .signWith(SignatureAlgorithm.HS512, secret) //签名的编码形式，盐
                .compact(); // 压紧，就是生成啦
    }

    /**
     * 根据token查出JWT中的 负载信息
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser() // 分析 -> 解密
                .setSigningKey(secret) // 你加密的盐
                .parseClaimsJws(token) // 你根据token解析出来
                .getBody(); //负载还有头信息 ，我们这里是需要它的主体
        } catch (Exception e) {
            log.info("JWT格式验证失败：{}",token);
        }
        return claims;
    }

    /**
     * 从token 中拿到 登录的用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        // 先查询出 负载信息，从负载信息中取出用户名
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 校验 token是否还有效  = 通过 token 解析的数据 是否是正确的 token 是否已过期
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String userNameFromToken = getUserNameFromToken(token);
        //条件 有两个
        //条件一 token 取出来的用户名 和 spring当前线程 中的用户名是不是一样的。
        //条件二 token 是否过期，超过过期 登录超时
        return userNameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        //获取 过期时间
        Date expiredDate = getExpiredDateFromToken(token);
        //当前时间 和 过期时间比较
        return expiredDate.before(new Date());
    }

    /**
     * 根据token 获取过期时间
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        //过期时间戳 也是存在 负载中 ，所以查询出负载 获取即可
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 生成过期时间
     * @return
     */
    private Date generateExpirationDate() {
        // 过期时间 = 当前时间 + 截止日期的时间戳
        return new Date(System.currentTimeMillis() + expiration);
    }

    /**
     * 根据用户信息 组装生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        //key -> value
        //key 统一配置好了
        //这里我们只存了用户名 ，后期如果需要可以存更多信息，当然我们可以把更多信息交给redis
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 当原来的token没过期是可以刷新 ，什么时候刷新，可以设置当每次最新请求进来的时候，
     * 耗费性能，这里可以根据指定时间外进来的 30分钟
     * @param oldToken 这里token 是 带有 token head 的token 也就是完全体
     * @return
     */
    public String refreshHeadToken(String oldToken) {
        //判断 token 是否为空
        if (StrUtil.isEmpty(oldToken)){
            return null;
        }
        //解析完的token
        String token = oldToken.substring(tokenHead.length());
        if (StrUtil.isEmpty(token)){
            return null;
        }
        //解析完 做校验
        // 解密实体
        Claims claims = getClaimsFromToken(token);
        if (claims == null){
            return null;
        }
        //如果解析出实体 ，就看token是否过期
        if (isTokenExpired(token)){
            return null;
        }
        //如果token这个请求30分钟内已经被刷新过了，直接返回原来token
        if (tokenRefreshJustBefore(token, REFRESHSECOND)){
            return token;
        }
        //否则刷新 刷新逻辑 就是重新设置过期时间啦
        claims.put(CLAIM_KEY_CREATED, new Date());
        //设置完时间 重新 生成token 返回啦 异步刷新，用户又不知道token发生变化
        return generateToken(claims);
    }

    /**
     * 判断token在指定时间是否刚刚刷新过
     * @param token
     * @param time
     * @return
     */
    private boolean tokenRefreshJustBefore(String token, int time){
        //获取jwt实体
        Claims claims = getClaimsFromToken(token);
        //获取创建时间
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date now = new Date();
        //条件一 当前时间 在创建时间之后 严谨 哈哈哈
        if (now.after(created)
                //条件二 判断 当前时间 是否在 刷新时间之外
                //这里 有个方法 相当于 两个时间相加 然后 生成新的时间
                && now.before(DateUtil.offsetSecond(created,time))){
            return true;
        }
        return false;
    }
}
