package com.lih.aspect;

import com.lih.annotcation.AddLog;
import com.lih.dao.LogDao;
import com.lih.entity.Admin;
import com.lih.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/24 18:11
 */
@Configuration
@Aspect
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    @Autowired
    private LogDao logDao;
    @Autowired
    private HttpServletRequest request;

    /**
     * AOP添加日志
     * @return
     */
    @Around("@annotation(com.lih.annotcation.AddLog)")
    public Object addLog(ProceedingJoinPoint proceedingJoinPoint){
        //获取管理员信息
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        log.info("获取的方法名: {}",methodName);
        //获取方法
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        //获取注解内容
        String value = method.getAnnotation(AddLog.class).value();
        HashMap<String, Object> map = new HashMap<>();
        map.put("message","操作成功");
        map.put("message","200");
        //执行方法
        String message = null;
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
            message = "SUCCESS";
        } catch (Throwable throwable) {
            map.put("message",throwable.getMessage());
            map.put("status","201");
            message = "ERROR";
        }

        //存入对象
        Log yxlog = new Log(UUID.randomUUID().toString(),admin.getNickname(),new Date(),methodName+" ("+value+")",message);

        log.info("存入的数据: {}",log);

        logDao.insertSelective(yxlog);
        if (result==null) return map;
        return result;
    }
}
