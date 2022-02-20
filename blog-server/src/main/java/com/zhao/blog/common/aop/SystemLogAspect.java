package com.zhao.blog.common.aop;

import com.zhao.blog.common.annotations.MyLogger;
import com.zhao.blog.domain.entity.SysLog;
import com.zhao.blog.domain.entity.SysUser;
import com.zhao.blog.service.ISysUserService;
import com.zhao.blog.service.IThreadPoolForLogService;
import com.zhao.blog.utils.DateThreadLocalUtils;
import com.zhao.blog.utils.HttpContextUtils;
import com.zhao.blog.utils.IpUtils;
import com.zhao.blog.utils.UserThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/25 11:21
 * @description 日志切面
 */

@Slf4j
@Aspect
@Component
public class SystemLogAspect {

    @Autowired
    private IThreadPoolForLogService threadPoolForLogService;

    @Autowired
    private ISysUserService userService;

    @Autowired(required = false)
    private HttpServletRequest request;

    /** 切点为自定义的注解 **/
    @Pointcut("@annotation(com.zhao.blog.common.annotations.MyLogger)")
    public void customPointcutForController(){}

    /**
     * 前置通知(在方法执行之前返回)用于拦截 Controller 层记录用户的操作的开始时间
     * @param joinPoint
     * @throws InterruptedException
     */
    @Before("customPointcutForController()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {

        log.info("=====================log start================================");
        /** 线程绑定变量（该数据只有当前请求的线程可见） **/
        DateThreadLocalUtils.put(new Date(System.currentTimeMillis()));
    }

    /**
     * 后置通知(在方法执行之后并返回数据)用于拦截 Controller 层无异常的操作
     * @param joinPoint 切点
     */
    @AfterReturning("customPointcutForController()")
    public void after(JoinPoint joinPoint) {

        try {
            Map<String, Object> controllerMethodInfo = getControllerMethodInfo(joinPoint);

            String token = request.getHeader("Authorization");

            SysLog sysLog = new SysLog();

            /** 判断当前是否是游客操作 **/
            if (!(null == token && token.equalsIgnoreCase(""))) {

                /** 不是游客操作 **/
                SysUser user = UserThreadLocalUtils.get();
                if (user != null) {
                    /** 不是游客操作就记录当前操作发起用户的电话号码和id **/
                    sysLog.setPhoneNumber(user.getPhoneNumber());
                    sysLog.setUserId(user.getId());
                }
            }

            /** 打印接收到的参数 **/
            log.info("params:{}",joinPoint.getArgs());
            /** 获取用户ip地址 **/
            HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
            log.info("ip:{}", IpUtils.getIpAddr(request));
            sysLog.setIp(IpUtils.getIpAddr(request));
            /** 设置日志对应方法 **/
            log.info("request method:{}", controllerMethodInfo.get("method").toString());
            sysLog.setMethod(controllerMethodInfo.get("method").toString());
            /** 设置日志对应模块 **/
            log.info("module:{}",controllerMethodInfo.get("module").toString());
            sysLog.setModule(controllerMethodInfo.get("module").toString());
            /** 设置日志操作 **/
            log.info("operation:{}",controllerMethodInfo.get("operation").toString());
            sysLog.setOperation(controllerMethodInfo.get("operation").toString());
            /** 设置操作用时 **/
            Long beginTime = DateThreadLocalUtils.get().getTime();
            Long endTime = System.currentTimeMillis();
            Long runTime = endTime - beginTime;
            log.info("execute time : {} ms", runTime);
            sysLog.setRunningTime(runTime);

            /** 日志持久化到数据库 **/
            threadPoolForLogService.saveLog(sysLog);

        } catch (Exception e) {

            log.error("AOP后置通知异常", e);
        } finally {

            log.info("=====================log end================================");
        }

    }

    /**
     * 至此获取到了当前注解的所有对标注类描述的内部信息
     * @param joinPoint
     * @return 注解内部对方法的描述
     * @throws Exception
     */
    public static Map<String, Object> getControllerMethodInfo(JoinPoint joinPoint) throws Exception {

        /** 新建哈希表来完成对于存储被标注方法的各个信息 **/
        Map<String, Object> map = new HashMap<String, Object>(16);
        /** 获取被标注类的类名 **/
        String targetName = joinPoint.getTarget().getClass().getName();
        /** 获取被标注类的方法明 **/
        String methodName = joinPoint.getSignature().getName();
        /** 获取被标注类的相关参数 **/
        Object[] arguments = joinPoint.getArgs();

        /** 通过前面获取的类名获取对应class对象 **/
        Class targetClass = Class.forName(targetName);
        /** 获取该类中的方法 **/
        Method[] methods = targetClass.getMethods();

        /** 自定义注解四属性 **/
        String methodReal = "";
        String module = "";
        String operation = "";
        Integer logType = null;

        for (Method method : methods) {

            /** 循环找到所有被该注解标注的方法 **/
            if (!method.getName().equals(methodName)) {

                continue;
            }

            /** 获取被标注方法形参类型的 class 对象数组 **/
            Class[] clazzs = method.getParameterTypes();

            /** 为了防止方法重载的发生 **/
            if (clazzs.length != arguments.length) {
                /** 比较一下当前方法中参数个数是否与切点中获取的参数个数相同 **/
                continue;
            }

            methodReal = targetName + methodName;
            module = method.getAnnotation(MyLogger.class).module();
            operation = method.getAnnotation(MyLogger.class).operation();

            map.put("method", methodReal);
            map.put("module", module);
            map.put("operation", operation);
        }

        /** 至此获取到了当前注解的所有对标注类描述的内部信息 **/
        return map;
    }

}
