package com.biao.web.valid;

import com.biao.exception.PlatException;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.util.CollectionHelp;
import com.biao.util.JsonUtils;
import com.biao.util.ReflectionUtil;
import com.biao.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Aspect
@Component
public class ParamerValidHandler {

    private static ConcurrentHashMap<String, ValidateGroupWrap> validCache = new ConcurrentHashMap<>(5000);

    @Pointcut("execution(public * com.biao.web.controller..*.*(..))")
    public void webValid() {
    }

    /**
     * 是否抛异常,默认抛异常
     */
    private boolean throwEception;

    private static final Logger logger = LoggerFactory.getLogger(ParamerValidHandler.class);


    private static String getCacheKey(String className, String methodName) {
        return new StringBuilder(className).append("_").append(methodName).toString();
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("webValid()")
    public Object aroundValid(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        String key = getCacheKey(target.getClass().getName(), joinPoint.getSignature().getName());
        ValidateGroupWrap validateGroupWrap = validCache.get(key);
        if (validateGroupWrap == null) {
            Method method = ReflectionUtil.getMethodByClassAndName(target.getClass(), joinPoint.getSignature().getName());
            ValidateGroup validateGroup = (ValidateGroup) ReflectionUtil.getAnnotationByMethod(method, ValidateGroup.class);
            if (validateGroup != null) {
                validateGroupWrap = new ValidateGroupWrap(validateGroup, true);
                validCache.put(key, validateGroupWrap);
            } else {
                validateGroupWrap = new ValidateGroupWrap(validateGroup, false);
                validCache.put(key, validateGroupWrap);
            }
        }
        if (!validateGroupWrap.isExist) {
            return joinPoint.proceed();
        }
        ValidateGroup validateGroup = validateGroupWrap.getValidateGroup();
        if (validateGroup != null && validateGroup.fileds() != null && validateGroup.fileds().length > 0) {
            //开启验证器
            if (logger.isDebugEnabled()) {
                logger.debug("验证key = {}", key);
            }
            ValidateFiled[] validateFileds = validateGroup.fileds();
            try {
                for (int i = 0; i < validateFileds.length; i++) {
                    ValidateFiled validateFiled = validateFileds[i];
                    if (validateFiled != null) {
                        String whenJson = validateFiled.when();
                        if (StringUtils.isBlank(whenJson)) {
                            validFiled(validateFiled, args[validateFiled.index()]);
                        } else {
                            validFiledWhen(validateFiled, args, args[validateFiled.index()], whenJson);
                        }
                    }
                }
            } catch (PlatException be) {
                logger.error("验证key = {},验证失败code:{},msg:{}", key, be.getCode(), be.getMsg());
                if (!throwEception) {
                    return Mono.just(GlobalMessageResponseVo.newInstance(be.getCode(), be.getMsg()));
                } else {
                    throw be;
                }
            } catch (Exception e) {
                logger.error("验证key = {},Exception:{}", key, e);
                if (!throwEception) {
                    return Mono.just(GlobalMessageResponseVo.newErrorInstance("系统暂停服务,请联系客服"));
                } else {
                    throw e;
                }
            }
        }
        return joinPoint.proceed();
    }

    /**
     * @param whenJson
     * @param i
     * @param args
     * @param validateFiled
     */
    private void validFiledWhen(ValidateFiled validateFiled, Object[] args, Object value, String whenJson) {
        Map<String, String> whenMap = JsonUtils.mapStrFromJson(whenJson);
        if (whenMap != null) {
            Object whenValue = null;
            String name = whenMap.get(ValidateFiled.WHEN_FILED_NAME);
            String index = whenMap.get(ValidateFiled.WHEN_FILED_INDEX);
            if (StringUtils.isNotBlank(name)) {
                whenValue = ReflectionUtil.getFieldValue(value, name);
            }
            if (StringUtils.isNotBlank(index) && StringUtil.regexPositiveValue(index)) {
                whenValue = args[Integer.parseInt(index)];
            }
            validFiled(validateFiled, value, whenValue, whenMap.get(ValidateFiled.WHEN_FILED_VALUE));
        } else {
            logger.error("验证参数注解配置属性when的值json解析错误");
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
    }

    private void validFiled(ValidateFiled validateFiled, Object value, Object when, String whenConfigValue) {
        if (when != null && whenConfigValue != null && (String.valueOf(when).equals(whenConfigValue))) {
            validFiled(validateFiled, value);
        }
    }

    /**
     * @param validateFiled
     * @param value
     */
    private void validFiled(ValidateFiled validateFiled, Object value) {
        Object validValue = null;
        if (StringUtils.isNotBlank(validateFiled.filedName())) {
            //验证对象属性
            validValue = ReflectionUtil.getFieldValue(value, validateFiled.filedName());
        } else {
            //验证基本类型
            validValue = value;
        }
        //开启验证参数值
        if (validateFiled.notNull() && validValue == null) {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
        if (validateFiled.isEnums() && validValue != null) {
            String enums = validateFiled.enums();
            List<String> enumList = CollectionHelp.arrayStrToStringList(enums, ",");
            String validValueStr = String.valueOf(validValue);
            if (enumList != null && !enumList.contains(validValueStr)) {
                validEnums(validateFiled, validValueStr, enumList);
            }
        }
        //对比属性
        if (StringUtils.isNotBlank(validateFiled.compareFiled())) {
            Object compareValue = ReflectionUtil.getFieldValue(value, validateFiled.compareFiled());
            if (validateFiled.compareNotNull() && compareValue == null) {
                throw new PlatException(validateFiled.code(), validateFiled.compareErrMsg());
            }
            validCompare(validateFiled, validValue, compareValue);
        }
        if (validValue instanceof String) {
            validString(validateFiled, (String) validValue);
        } else if (validValue instanceof Integer) {
            validInt(validateFiled, (Integer) validValue);
        } else if (validValue instanceof Long) {
            validLong(validateFiled, (Long) validValue);
        } else if (validValue instanceof Double) {
            validDouble(validateFiled, (Number) validValue);
        } else if (validValue instanceof Float) {
            validDouble(validateFiled, (Number) validValue);
        }
    }

    private void validEnums(ValidateFiled validateFiled, String validValueStr, List<String> enumList) {
        //验证逗号隔开的多个枚举值
        if (!validateFiled.enumsList()) {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
        int index = validValueStr.indexOf(",");
        if (index != -1) {
            boolean flag = false;
            List<String> validValueEnums = CollectionHelp.arrayStrToStringList(validValueStr, ",");
            for (String validValueEnum : validValueEnums) {
                if (!enumList.contains(validValueEnum)) {
                    flag = true;
                }
            }
            if (flag) {
                throw new PlatException(validateFiled.code(), validateFiled.errMsg());
            }
        } else {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
    }

    private void validCompare(ValidateFiled validateFiled, Object validValue, Object compareValue) {
        if (validValue == null) {
            return;
        }
        if ((validValue instanceof Date) && validateFiled.compareLt()) {
            //对比小于时间
            if (validateFiled.compareNowTime() && ((Date) validValue).getTime() > new Date().getTime()) {
                throw new PlatException(validateFiled.code(), validateFiled.compareErrMsg());
            }
            //对比小于
            if ((compareValue instanceof Date) && compareValue != null && ((Date) validValue).getTime() >= ((Date) compareValue).getTime()) {
                throw new PlatException(validateFiled.code(), validateFiled.compareErrMsg());
            }
        }
    }

    private void validString(ValidateFiled validateFiled, String validValue) {
        if (validateFiled.notNull() && StringUtils.isBlank(validValue)) {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
        if (validValue == null) {
            return;
        }
        if (validateFiled.maxLen() > 0 && validValue.length() > validateFiled.maxLen()) {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
        if (validateFiled.minLen() > 0 && validValue.length() < validateFiled.minLen()) {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
        if (StringUtils.isNotBlank(validateFiled.regStr()) && !StringUtil.regexMatcher(validateFiled.regStr(), validValue)) {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
    }

    private void validInt(ValidateFiled validateFiled, Integer validValue) {
        if (validValue == null) {
            return;
        }
        if (validateFiled.maxVal() > 0 && validValue > validateFiled.maxVal()) {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
        if (validateFiled.minVal() != (-999999999) && validValue < validateFiled.minVal()) {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
        validString(validateFiled, String.valueOf(validValue));
    }

    /**
     * @param validateFiled
     * @param validValue
     */
    private void validDouble(ValidateFiled validateFiled, Number validValue) {
        if (validValue == null) {
            return;
        }
        if (validateFiled.minVal() != (-999999999) && validValue.doubleValue() < validateFiled.minVal()) {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
        validString(validateFiled, String.valueOf(validValue));
    }

    /**
     * @param validateFiled
     * @param validValue
     */
    private void validLong(ValidateFiled validateFiled, Long validValue) {
        if (validValue == null) {
            return;
        }
        if (validateFiled.minVal() != (-999999999) && validValue < validateFiled.minVal()) {
            throw new PlatException(validateFiled.code(), validateFiled.errMsg());
        }
        validString(validateFiled, String.valueOf(validValue));
    }

    /**
     * @param throwEception the throwEception to set
     */
    public void setThrowEception(boolean throwEception) {
        this.throwEception = throwEception;
    }

    public static class ValidateGroupWrap {

        private ValidateGroup validateGroup;

        private boolean isExist;

        public ValidateGroupWrap(ValidateGroup validateGroup, boolean isExist) {
            super();
            this.validateGroup = validateGroup;
            this.isExist = isExist;
        }

        public ValidateGroup getValidateGroup() {
            return validateGroup;
        }

        public void setValidateGroup(ValidateGroup validateGroup) {
            this.validateGroup = validateGroup;
        }

        public boolean isExist() {
            return isExist;
        }

        public void setExist(boolean isExist) {
            this.isExist = isExist;
        }
    }
}
