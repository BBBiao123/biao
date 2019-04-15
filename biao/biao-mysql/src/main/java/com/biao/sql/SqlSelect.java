package com.biao.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 对象查询注解
 * ClassName: SqlSelect <br/>
 * Function:  <br/>
 * date: 2017-3-20 上午9:47:19 <br/>
 *
 *  ""oury
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlSelect {

    /**
     * 字段名称
     *
     * @return
     */
    String value();

    /**
     * 自动构建类型
     *
     * @return
     */
    int[] type() default {0};

    /**
     * 查询类型
     *
     * @return
     */
    SelectType selectType() default SelectType.EQUAL;
}
