package me.erudev.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import me.erudev.order.dao.OrderDetailDao;
import me.erudev.order.dto.OrderMessageDTO;
import me.erudev.order.enums.OrderStatus;
import me.erudev.order.po.OrderDetailPO;
import me.erudev.order.vo.OrderCreateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * @author pengfei.zhao
 * @date 2021/2/11 15:33
 */
@Service
public class OrderService {

    @Autowired
    private OrderDetailDao orderDetailDao;

    ObjectMapper objectMapper = new ObjectMapper();

    public void createOrder(OrderCreateVO orderCreateVO) throws IOException, TimeoutException {
        OrderDetailPO order = new OrderDetailPO();
        order.setAddress(orderCreateVO.getAddress());
        order.setAccountId(orderCreateVO.getAccountId());
        order.setProductId(orderCreateVO.getProductId());
        order.setStatus(OrderStatus.ORDER_CREATING);
        order.setDate(new Date());
        orderDetailDao.insert(order);

        OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
        orderMessageDTO.setOrderId(order.getId());
        orderMessageDTO.setProductId(order.getProductId());
        orderMessageDTO.setAccountId(orderCreateVO.getAccountId());

        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection conn = connectionFactory.newConnection();
        Channel channel = conn.createChannel();

        String msgToSend = objectMapper.writeValueAsString(orderMessageDTO);

        channel.basicPublish("exchange.order.restaurant",
                "key.order",
                null,
                msgToSend.getBytes());
    }
}
