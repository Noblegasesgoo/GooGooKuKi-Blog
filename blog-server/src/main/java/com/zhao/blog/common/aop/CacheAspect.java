package com.zhao.blog.common.aop;

import com.alibaba.fastjson.JSON;
import com.zhao.blog.common.annotations.MyCache;
import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/28 11:35
 * @description 统一缓存切面处理
 */

@Slf4j
@Aspect
@Component
public class CacheAspect {

    /** 选用 redis 做一级缓存 **/
    @Autowired
    private StringRedisTemplate redisTemplate;

    /** 设置切点 **/
    @Pointcut("@annotation(com.zhao.blog.common.annotations.MyCache)")
    public void pointcut(){}

    /** 环绕通知 **/
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint){

        try {

            /** 获取切点签名 **/
            Signature signature = proceedingJoinPoint.getSignature();
            /** 获取切点标注的类名 **/
            String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
            /** 切点注释的方法名 **/
            String methodName = signature.getName();

            /** 下面的步骤意在使用参数生成 redis 中不同的 key，以及对应参数的方法 **/
            /** 用于获取标注方法的参数类型 **/
            Class[] parameterTypes = new Class[proceedingJoinPoint.getArgs().length];
            /** 用于获取标注方法的参数 **/
            Object[] args = proceedingJoinPoint.getArgs();
            /** 循环将参数所对应的类型装入 **/
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                /** 如果当前这个参数有值的话 **/
                if (args[i] != null) {

                    /** 将参数转换为 JSON 便于显示（不排除参数是类的情况）**/
                    params.append(JSON.toJSONString(args[i]));

                    /** 设置参数类型 **/
                    parameterTypes[i] = args[i].getClass();
                } else {
                    /** 当前这个参数没值，对应类型设为 null **/
                    parameterTypes[i] = null;
                }
            }

            String param = null;
            /** 加密参数，防止key过长的情况以及字符串转义获取不到的情况 **/
            if (StringUtils.isNotEmpty(params)) {
                param = DigestUtils.md5Hex(params.toString());
            }

            /** 根据对应的参数，对应方法名称，得到指定的方法 **/
            Method method = proceedingJoinPoint.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);

            /** 获取标注方法上的自定义缓存注解 **/
            MyCache myCache = method.getAnnotation(MyCache.class);
            /** 获取缓存名称 **/
            String name = myCache.name();
            /** 获取缓存有效时间 **/
            long expire = myCache.expire();

            /** 尝试先从 redis 获取对应信息的缓存 **/
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(name).append("::").append(className).append("::").append(methodName).append("::").append(param);
            String redisKey = stringBuilder.toString();

            /** redis 对应 key 找对应 value **/
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            /** 如果找到，直接返回缓存数据 **/
            if (StringUtils.isNotEmpty(redisValue)){
                log.info("本次是从缓存中获取,缓存的key为：{}", redisKey);
                Response response = JSON.parseObject(redisValue, Response.class);
                return response;
            }

            /** 获取该标注方法返回的结果 **/
            Object proceed = proceedingJoinPoint.proceed();

            /** 将其存入缓存 **/
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("缓存中暂无，存入缓存,{},{}",className,methodName);

            return proceed;

        } catch (Throwable throwable) {

            throwable.printStackTrace();
        }

        return new Response(StatusCode.STATUS_CODEC500.getCode(), "系统内部缓存错误！", null);
    }

}


