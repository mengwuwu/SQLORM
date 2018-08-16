package com.example.wumeng.testjava.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by WuMeng on 2018/8/10.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TablePrimaryId {
    String value();
    //判断id是否自动增加
    boolean isAutoIncrease();
}
