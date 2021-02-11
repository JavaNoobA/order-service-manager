package me.erudev.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"me.erudev.order.dao"})
public class OrderServiceManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceManagerApplication.class, args);
    }

}
