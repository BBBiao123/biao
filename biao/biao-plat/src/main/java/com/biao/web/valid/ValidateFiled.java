package com.biao.web.valid;

import com.biao.constant.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidateFiled {

    public static final String WHEN_FILED_NAME = "filedName";

    public static final String WHEN_FILED_INDEX = "filedIndex";

    public static final String WHEN_FILED_VALUE = "value";

    /**
     * 参数索引位置 ,从0开始
     */
    public int index();

    /**
     * 如果参数是基本数据类型或String ，就不用指定该参数，如果参数是对象，要验证对象里面某个属性，就用该参数指定属性名
     */
    public String filedName() default "";

    /**
     * 是否是枚举
     *
     * @return
     */
    public boolean isEnums() default false;

    /**
     * 是否是多少个枚举
     *
     * @return
     */
    public boolean enumsList() default false;

    /**
     * 枚举字符串集合,采用逗号分隔
     *
     * @return
     */
    public String enums() default "";

    /**
     * 对比属性
     *
     * @return
     */
    public String compareFiled() default "";

    /**
     * 对比属性是否是小于,true不能为小于
     *
     * @return
     */
    public boolean compareLt() default false;

    /**
     * 对比属性是否为null,true不能为空
     */
    public boolean compareNotNull() default false;

    /**
     * 对比属性是否为当前时间,true对比小于当前时间
     */
    public boolean compareNowTime() default false;

    /**
     * 异常消息
     *
     * @return
     */
    public String compareErrMsg() default "";

    /**
     * 正则验证
     */
    public String regStr() default "";

    /**
     * 是否为null,true不能为空
     */
    public boolean notNull() default false;

    /**
     * 字符长度最大长度 (默认225,开启)
     */
    public int maxLen() default 225;

    /**
     * 字符长度最小长度
     */
    public int minLen() default -1;

    /**
     * 最大值 ，用于验证数字类型数据 (默认999999999,开启)
     */
    public int maxVal() default 999999999;

    /**
     * 最小值 ，用于验证数值类型数据
     */
    public int minVal() default -999999999;

    /**
     * 异常消息
     *
     * @return
     */
    public String errMsg() default "请输入合法的参数";

    /**
     * 异常消息
     *
     * @return
     */
    public int code() default Constants.PARAM_ERROR;

    /**
     * 当其他属性值有值时验证</br>
     * {filedName:xx,value:xx}或者{filedIndex:xx,value:xx}</br>
     * FiledName:其他属性的名称
     * value:其他属性的值(值都需要为字符串)</br>
     *
     * @return
     */
    public String when() default "";
}
