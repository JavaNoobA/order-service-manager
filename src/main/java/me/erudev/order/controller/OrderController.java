package me.erudev.order.controller;

import lombok.extern.slf4j.Slf4j;
import me.erudev.order.service.OrderService;
import me.erudev.order.vo.OrderCreateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author pengfei.zhao
 * @date 2021/2/11 15:41
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderCreateVO orderCreateVO) throws IOException, TimeoutException {
        log.info("createOrder:orderCreateDTO:{}", orderCreateVO);
        orderService.createOrder(orderCreateVO);
    }
}
