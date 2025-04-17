package com.eduplatform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ExtendedLombok {
    // 用于标记需要额外Lombok功能的类
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@interface ValidEmail {
    String message() default "Invalid email format";
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@interface ValidPassword {
    int minLength() default 8;
    String message() default "Password must be at least 8 characters";
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@interface SensitiveData {
    // 标记敏感数据，在日志和输出中需要隐藏
}