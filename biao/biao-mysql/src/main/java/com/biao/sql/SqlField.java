package com.biao.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段标识
 * ClassName: SqlField <br/>
 * Function:  <br/>
 * date: 2017-3-14 下午5:17:32 <br/>
 *
 *  ""oury
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlField {

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
     * jdbcType类型
     */
    String jdbcType() default "";

    /**
     * 别名
     *
     * @return
     */
    String alias() default "";

    /**
     * 更新是否跳过null或者""值
     *
     * @return
     */
    boolean updateBreakNull() default true;
}
