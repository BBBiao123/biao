package com.biao.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ReflectionUtil {

    /**
     * 获取所有属性集合(包含父类)
     *
     * @param clazz
     * @return
     */
    public static Field[] getAllField(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        allFields.addAll(Arrays.asList(fields));
        for (Class<?> superClazz = clazz.getSuperclass(); superClazz != Object.class; superClazz = superClazz.getSuperclass()) {
            Field[] superFields = superClazz.getDeclaredFields();
            allFields.addAll(Arrays.asList(superFields));
        }
        return allFields.toArray(new Field[allFields.size()]);
    }

    /**
     * 获取所有方法集合(包含父类)
     *
     * @param clazz
     * @return
     */
    public static Method[] getAllMethod(Class<?> clazz) {
        List<Method> allMethods = new ArrayList<>();
        Method[] fields = clazz.getDeclaredMethods();
        allMethods.addAll(Arrays.asList(fields));
        for (Class<?> superClazz = clazz.getSuperclass(); superClazz != Object.class; superClazz = superClazz.getSuperclass()) {
            Method[] superFields = superClazz.getDeclaredMethods();
            allMethods.addAll(Arrays.asList(superFields));
        }
        return allMethods.toArray(new Method[allMethods.size()]);
    }

    /**
     * 获取属性的值
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            if (obj instanceof Collection<?>) {
                return getCollectionFieldValue((Collection<?>) obj, fieldName);
            }
            if (obj instanceof Integer) {
                return obj;
            }
            if (obj instanceof Long) {
                return obj;
            }
            if (obj instanceof String) {
                return obj;
            }
            if (obj instanceof Boolean) {
                return obj;
            }
            Object value = PropertyUtils.getProperty(obj, fieldName);
            if (value == null) {
                return null;
            }
            if ((value instanceof String) && StringUtils.isEmpty(value.toString())) {
                return null;
            }
            return value;
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
        return null;
    }

    private static Object getCollectionFieldValue(Collection<?> collection, String fieldName) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        Object[] arrays = collection.toArray();
        int index = fieldName.indexOf(".");
        if (index != -1) {
            String indexObj = fieldName.substring(0, index);
            String indexField = fieldName.substring(index + 1);
            if ("last".equals(indexObj)) {
                return getFieldValue(arrays[arrays.length - 1], indexField);
            }
            return getFieldValue(arrays[Integer.parseInt(indexObj)], indexField);
        }
        return getFieldValue(arrays[0], fieldName);
    }

    /**
     * 根据目标方法和注解类型  得到该目标方法的指定注解
     */
    public static Annotation getAnnotationByMethod(Method method, Class<?> annoClass) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annoClass) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * @param clazz
     * @param methodName
     * @return
     */
    public static Method getMethodByClassAndName(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
