package com.nenu.dsms.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 自定义注解 被这个注解修饰的接口不需要登录态即可访问
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSignIn {
}
