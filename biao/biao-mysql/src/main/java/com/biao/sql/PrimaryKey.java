package com.biao.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键标识
 * ClassName: PrimaryKey <br/>
 * Function:  <br/>
 * date: 2017-3-15 上午9:59:08 <br/>
 *
 *  ""oury
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {

    /**
     * 插入是否跳过主键
     *
     * @return
     */
    boolean insertIsSkip() default true;

    /**
     * 更新是否跳过
     *
     * @return
     */
    boolean updateIsSkip() default true;
}
