package com.zhao.blog.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/2/10 12:34
 * @description redis配置类
 */

@Configuration
public class RedisConfig {

    ///**
    // * 修改Redis默认的序列化方式，默认文件在RedisAutoConfiguration
    // * @param redisConnectionFactory
    // * @return RedisTemplate
    // */
    //@Bean
    //public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
    //
    //    RedisTemplate redisTemplate = new RedisTemplate();
    //    redisTemplate.setConnectionFactory(redisConnectionFactory);
    //
    //    /** 设置key的序列化方式为string **/
    //    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    //    redisTemplate.setKeySerializer(stringRedisSerializer);
    //
    //    /** 设置value的序列化方式为json **/
    //    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    //
    //    /** 定制化关于时间格式序列化问题 **/
    //    ObjectMapper objectMapper = new ObjectMapper();
    //    SimpleModule simpleModule = new SimpleModule();
    //    simpleModule.addSerializer(Date.class,new JodaDateTimeJsonSerializer());
    //    simpleModule.addDeserializer(Date.class,new JodaDateTimeJsonDeSerializer());
    //    objectMapper.registerModule(simpleModule);
    //    //在保存结果中加入类信息，方便解析数据
    //    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    //    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    //    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    //    return redisTemplate;
    //}

}
