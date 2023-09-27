package com.kuafoo4j.example;

import com.kuafoo4j.example.service.OrderService;
import com.kuafoo4j.example.service.UserService;
import cpm.kuafoo4j.spring.SpringApplicationContext;

/**
 * @description:
 * @author: zk
 * @date: 2023-09-27 15:14
 */
public class Application {
    public static void main(String[] args) {
        SpringApplicationContext applicationContext = new SpringApplicationContext(Config.class);

        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();
        userService.orderTest();

        OrderService orderService = (OrderService) applicationContext.getBean("orderService");
        orderService.test();

    }
}
