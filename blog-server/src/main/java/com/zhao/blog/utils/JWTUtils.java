package com.zhao.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/20 13:48
 * @description JWT工具类
 */

public class JWTUtils {

    private static final String secretKey = "zhaoliminwanglu1314!@#$$";

    /**
     * 创建 JWT token
     * @param userId
     * @return token
     */
    public static String createToken(Long userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey) // 签发算法，秘钥为 secretKey
                .setClaims(claims) // 载荷(body)
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));// 一天的有效时间
        String token = jwtBuilder.compact();
        return token;
    }

    /**
     * 解析 token
     * @param token
     * @return 解析后的 body 内容
     */
    public static Map<String, Object> checkToken(String token){

        try {

            Jwt parse = Jwts.parser().setSigningKey(secretKey).parse(token);

            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){

            e.printStackTrace();
        }

        return null;
    }
}
