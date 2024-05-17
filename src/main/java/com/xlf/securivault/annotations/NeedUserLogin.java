package com.xlf.securivault.annotations;


import java.lang.annotation.*;

/**
 * 该注解用于标记需要登录的用户
 * <hr/>
 * 用来标记该接口需要登录才能访问，如果用户未登录则会被拦截，返回未登录的错误信息，如果用户已登录则会继续访问
 * 该注解只允许标记在方法上, 不允许标记在类上
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedUserLogin {

    /**
     * 是否需要登录
     */
    boolean value() default true;
}
