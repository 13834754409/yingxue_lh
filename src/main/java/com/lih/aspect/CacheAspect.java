package com.lih.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/27 19:41
 */
@Configuration
@Aspect
public class CacheAspect {
    @Autowired
    RedisTemplate redisTemplate;

    //添加缓存
    @Around("@annotation(com.lih.annotcation.AddCache)")
    public Object addCache(ProceedingJoinPoint proceedingJoinPoint){
        System.out.println("进入缓存");

        //序列化解决
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        StringBuilder sb = new StringBuilder();

        //获取类名
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        sb.append(methodName);
        //获取参数
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            sb.append(arg);
        }
        //存的key
        String key = sb.toString();
        //判断key是否存在
        HashOperations hashOperations = redisTemplate.opsForHash();
        Boolean aBoolean = hashOperations.hasKey(className, key);

        Object result = null;
        if(aBoolean){
            //key存在
            result = hashOperations.get(className, key);
            //从redis中取出缓存数据
        }else {
            //key不存在
            try {
                //放行方法
                result = proceedingJoinPoint.proceed();
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }
            //加入缓存 放入redis
            hashOperations.put(className,key,result);
        }

        return result;
    }

    //清除缓存
    @After("@annotation(com.lih.annotcation.DelCache)")
    public void delCache(JoinPoint joinPoint){
        System.out.println("清楚缓存");

        //获取类的全限定名
        String className = joinPoint.getTarget().getClass().getName();

        //清除缓存
        redisTemplate.delete(className);
    }



}
