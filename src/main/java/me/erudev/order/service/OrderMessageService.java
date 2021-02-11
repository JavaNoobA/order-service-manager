package me.erudev.order.service;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
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

    public static void handleMessage() {
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

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }


}
