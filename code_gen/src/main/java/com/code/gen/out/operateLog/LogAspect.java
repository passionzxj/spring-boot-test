package com.code.gen.out.operateLog;

import com.code.gen.out.annotation.OperLog;
import com.code.gen.out.annotation.OperationLog;
import com.code.gen.out.annotation.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private OperationLogService operationLogService;

    @Pointcut("@annotation(com.code.gen.out.annotation.OperLog)")
    public void logPointCut() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

    /**
     * 后置通知 用于拦截操作，在方法返回后执行,可以获取返回结果
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturn(JoinPoint joinPoint) {
//        handleLog(joinPoint, null);
    }

    /**
     * 拦截异常操作，有异常时执行
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e);
    }

    /**
     * 数据获取以及入库
     *
     * @param joinPoint
     * @param e
     */
    private void handleLog(JoinPoint joinPoint, Exception e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //请求的IP
        String ip = this.getIpAddr(request);
        // 引入下面获取注解的方法对象
        OperationLog operlog = new OperationLog();
        try {
            OperLog operLog = getAnnotationLog(joinPoint);
            if (operLog != null) {
                String operModule = operLog.operModule();
                String operType = operLog.operType();
                String operDesc = operLog.operDesc();
                operlog.setOperModule(operModule); // 操作模块
                operlog.setOperType(operType); // 操作类型
                operlog.setOperDesc(operDesc); // 操作描述
            }

            String methodName = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()";
            log.info("请求方法:{}" + methodName);

            // 访问方法名
            operlog.setId(UUID.randomUUID().toString());
            operlog.setOperMethod(methodName); // 请求方法

            //@TODO 入库操作
            operlog.setOperUserId("123456"); // 请求用户ID
            operlog.setOperUserName("admin"); // 请求用户名称
            operlog.setOperIp(ip); // 请求IP
            operlog.setOperUri(request.getRequestURI()); // 请求URI
            operlog.setOperCreateTime(new Date()); // 创建时间
            operationLogService.insert(operlog);

        } catch (Exception exp) {
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private static OperLog getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(OperLog.class);
        }
        return null;
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Real-Ip");//先从nginx自定义配置获取
        log.info("用户真实ip: {}" + ipAddress);
        String unknown = "unknown";
        if (ipAddress == null || ipAddress.length() == 0 || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("x-forwarded-for");
        }
        if (ipAddress == null || ipAddress.length() == 0 || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            String benji = "127.0.0.1";
            String bj = "0:0:0:0:0:0:0:1";
            if (benji.equals(ipAddress) || bj.equals(ipAddress)) {
                ///根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (inet != null) {
                    ipAddress = inet.getHostAddress();
                }
            }
        }
        ///对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        int i = 15;
        String s = ",";
        if (ipAddress != null && ipAddress.length() > i) {
            if (ipAddress.indexOf(s) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

}