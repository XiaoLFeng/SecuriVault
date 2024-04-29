package com.xlf.securivault.config.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 日志切面
 * <hr/>
 * 用于记录系统中的日志信息，用于记录系统中的日志信息，该日志信息包含了系统中的操作信息、操作时间、操作人员等信息；
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggerAspect {

    /**
     * 记录控制器请求日志
     * <hr/>
     * 用于记录控制器请求的日志，包括控制器请求的基本信息、请求参数等；该接口主要用于对控制器请求的日志进行记录；
     *
     * @param joinPoint 切点
     */
    @Before("execution(* com.xlf.securivault.controllers.*.*(..))")
    public void controllerLog(@NotNull JoinPoint joinPoint) {
        String getSimpleClassName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String getFuncName = joinPoint.getSignature().getName();
        log.info("[CONTROL] <{}>{} | 请求参数 {} 个", getSimpleClassName, getFuncName, joinPoint.getArgs().length);
    }

    /**
     * 记录服务请求日志
     * <hr/>
     * 用于记录服务请求的日志，包括服务请求的基本信息、请求参数等；该接口主要用于对服务请求的日志进行记录；
     *
     * @param joinPoint 切点
     */
    @Before("execution(* com.xlf.securivault.services.*.*(..))")
    public void serviceLog(@NotNull JoinPoint joinPoint) {
        String getSimpleClassName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String getFuncName = joinPoint.getSignature().getName();
        log.info("[SERVICE] <{}>{} | 请求参数 {} 个", getSimpleClassName, getFuncName, joinPoint.getArgs().length);
    }

    /**
     * 记录工具请求日志
     * <hr/>
     * 用于记录工具请求的日志，包括工具请求的基本信息、请求参数等；该接口主要用于对工具请求的日志进行记录；
     *
     * @param joinPoint 切点
     */
    @Before("execution(* com.xlf.securivault.utility.*.*(..))")
    public void utilityLog(@NotNull JoinPoint joinPoint) {
        String getSimpleClassName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String getFuncName = joinPoint.getSignature().getName();
        log.info("[UTILITY] <{}>{} | 请求参数 {} 个", getSimpleClassName, getFuncName, joinPoint.getArgs().length);
    }

    /**
     * 记录DAO请求日志
     * <hr/>
     * 用于记录DAO请求的日志，包括配置请求的基本信息、请求参数等；该接口主要用于对DAO请求的日志进行记录；
     *
     * @param joinPoint 切点
     */
    @Before("execution(* com.xlf.securivault.dao.*.*(..))")
    public void daoLog(@NotNull JoinPoint joinPoint) {
        String getSimpleClassName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String getFuncName = joinPoint.getSignature().getName();
        log.info("[DAO] <{}>{} | 请求参数 {} 个", getSimpleClassName, getFuncName, joinPoint.getArgs().length);
    }
}
