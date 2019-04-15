package com.biao.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *  ""oury
 * @Function: 用于集成spring
 * @ClassName: SpringBeanFactoryContext
 * @date 2018年4月9日    下午2:12:11
 * @since JDK 1.7
 */
@Component
public class SpringBeanFactoryContext implements ApplicationContextAware {


    protected static ApplicationContext context;

    public static <T> T findBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static Object findBean(String name) {
        return context.getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
