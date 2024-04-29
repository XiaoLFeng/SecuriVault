package com.xlf.securivault.config.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 时间统计切面
 * <hr/>
 * 用于统计接口请求时间的切面，用于统计接口请求的时间，包括接口请求的开始时间、结束时间等；该接口主要用于对接口请求的时间进行统计；
 * 对接口进行分析，优化接口请求时间，提高接口请求的效率；
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TimeStatisticsAspect {

    @Around("execution(* com.xlf.securivault.controllers..*.*(..))")
    public Object timeStatistics(@NotNull ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object getResult = pjp.proceed();
        long endTime = System.currentTimeMillis();
        log.debug(
                "[TIME] <{}>{} | 接口耗时 {}ms",
                pjp.getSignature().getDeclaringType().getSimpleName(),
                pjp.getSignature().getName(),
                endTime - startTime
        );
        return getResult;
    }
}
