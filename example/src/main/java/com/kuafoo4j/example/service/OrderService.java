package com.kuafoo4j.example.service;


import cpm.kuafoo4j.spring.annatation.Component;
import cpm.kuafoo4j.spring.annatation.Lazy;
import cpm.kuafoo4j.spring.ext.BeanPostProcessor;

/**
 * @description:
 * @author: zk
 * @date: 2023-09-27 15:15
 */
@Lazy
@Component
public class OrderService implements BeanPostProcessor {

    private String beforeInitialization;
    private String afterInitialization;

    public void test() {
        System.out.println("OrderService#"+beforeInitialization+"#"+afterInitialization);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        beforeInitialization = "beforeInitialization" + beanName;
        System.out.println("postProcessBeforeInitialization:"+beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        afterInitialization = "afterInitialization" + beanName;
        System.out.println("postProcessAfterInitialization:"+beanName);
        return bean;
    }
}
