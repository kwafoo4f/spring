package com.kuafoo4j.example.service;

import cpm.kuafoo4j.spring.annatation.Autowired;
import cpm.kuafoo4j.spring.annatation.Component;
import cpm.kuafoo4j.spring.ext.InitializingBean;

/**
 * @description:
 * @author: zk
 * @date: 2023-09-27 15:15
 */
@Component
public class UserService implements InitializingBean {
    @Autowired
    private OrderService orderService;

    private String afterPropertiesSet;

    public void test() {
        System.out.println(afterPropertiesSet);
    }

    public void orderTest() {
        orderService.test();
    }

    @Override
    public void afterPropertiesSet() {
        afterPropertiesSet = "InitializingBean#afterPropertiesSet";
        System.out.println("afterPropertiesSet");
    }
}
