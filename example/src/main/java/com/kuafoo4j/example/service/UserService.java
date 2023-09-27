package com.kuafoo4j.example.service;

import cpm.kuafoo4j.spring.annatation.Autowired;
import cpm.kuafoo4j.spring.annatation.Component;

/**
 * @description:
 * @author: zk
 * @date: 2023-09-27 15:15
 */
@Component
public class UserService {
    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println("UserService#test");
    }

    public void orderTest() {
        orderService.test();
    }
}
