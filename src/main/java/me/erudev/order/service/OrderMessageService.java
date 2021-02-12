package me.erudev.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import me.erudev.order.dao.OrderDetailDao;
import me.erudev.order.dto.OrderMessageDTO;
import me.erudev.order.enums.OrderStatus;
import me.erudev.order.po.OrderDetailPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author pengfei.zhao
 * @date 2021/2/11 13:35
 */
@Service
@Slf4j
public class OrderMessageService {

    @Autowired
    private OrderDetailDao orderDetailDao;

    ObjectMapper objectMapper = new ObjectMapper();

    @Async
    public void handleMessage() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try (Connection conn = connectionFactory.newConnection();
             Channel channel = conn.createChannel()) {

            /*---------------------restaurant---------------------*/
            channel.exchangeDeclare("exchange.order.restaurant",
                    BuiltinExchangeType.DIRECT,
                    true,
                    false,
                    null);


            channel.queueDeclare("queue.order",
                    true,
                    false,
                    false,
                    null);

            channel.queueBind("queue.order", "exchange.order.restaurant", "key.order");

            /*---------------------deliveryman---------------------*/
            channel.exchangeDeclare("exchange.order.deliveryman",
                    BuiltinExchangeType.DIRECT,
                    true,
                    false,
                    null);


            channel.queueBind("queue.order", "exchange.order.deliveryman", "key.order");

            channel.basicConsume("queue.order", true, deliverCallback, consumerTag -> {
            });

            while (true) {
                Thread.sleep(100000);
            }
        }
    }

    DeliverCallback deliverCallback = (consumer, message) -> {
        String messageBody = new String(message.getBody());
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try {
            OrderMessageDTO orderMessageDTO = objectMapper.readValue(messageBody, OrderMessageDTO.class);
            OrderDetailPO orderPO = orderDetailDao.selectOrder(orderMessageDTO.getOrderId());
            switch (orderMessageDTO.getOrderStatus()) {
                case ORDER_CREATING:
                    if (orderMessageDTO.getConfirmed() && orderMessageDTO.getPrice() != null) {
                        orderPO.setPrice(orderMessageDTO.getPrice());
                        orderPO.setStatus(OrderStatus.RESTAURANT_CONFIRMED);
                        orderDetailDao.update(orderPO);

                        try (Connection conn = connectionFactory.newConnection();
                             Channel channel = conn.createChannel()) {
                            String msg = objectMapper.writeValueAsString(orderMessageDTO);
                            channel.basicPublish("exchange.order.deliveryman",
                                    "key.deliveryman",
                                    null,
                                    msg.getBytes());
                        }
                    } else {
                        orderPO.setStatus(OrderStatus.FAILED);
                        orderDetailDao.update(orderPO);
                    }
                    break;
                case RESTAURANT_CONFIRMED:
                    if (null != orderMessageDTO.getDeliverymanId()) {
                        orderPO.setStatus(OrderStatus.DELIVERYMAN_CONFIRMED);
                        orderPO.setDeliverymanId(orderMessageDTO.getDeliverymanId());
                        orderDetailDao.update(orderPO);
                        try (Connection connection = connectionFactory.newConnection();
                             Channel channel = connection.createChannel()) {
                            String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
                            channel.basicPublish("exchange.order.settlement", "key.settlement", null,
                                    messageToSend.getBytes());
                        }
                    } else {
                        orderPO.setStatus(OrderStatus.FAILED);
                        orderDetailDao.update(orderPO);
                    }
                    break;
                case DELIVERYMAN_CONFIRMED:
                    break;
                case SETTLEMENT_CONFIRMED:
                    break;
                case ORDER_CREATED:
                    break;
                case FAILED:
                    break;
            }

        } catch (JsonProcessingException | TimeoutException e) {
            e.printStackTrace();
        }
    };
}
