package com.biao.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表名注解
 * ClassName: SqlTable <br/>
 * Function:  <br/>
 * date: 2017-3-14 下午6:07:46 <br/>
 *
 *  ""oury
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SqlTable {

    /**
     * 表名称
     *
     * @return
     */
    String value();

    /**
     * 别名
     *
     * @return
     */
    String alias() default "";
}
