package com.biao.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * <b>功能说明:分销规则实例生成工厂
 */
@Component("mkDistributeBeanFactory")
public class MkDistributeBeanFactory implements BeanFactoryAware {

    private BeanFactory beanFactory;

    /**
     * 服务名称
     *
     * @param serviceName
     * @return
     */
    public Object getService(String serviceName) {
        return beanFactory.getBean(serviceName);
    }

    /**
     * 根据服务名称具体实体类
     *
     * @param serviceName 支付通道编码
     * @return
     */
    public <T> T getService(String serviceName, Class<T> requiredType) {
        return beanFactory.getBean(serviceName, requiredType);
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
