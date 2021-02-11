package me.erudev.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.erudev.order.enums.OrderStatus;

import java.math.BigDecimal;

/**
 * @author pengfei.zhao
 * @date 2021/2/11 13:13
 */
@Getter
@Setter
@ToString
public class OrderMessageDTO {
    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单状态
     */
    private OrderStatus orderStatus;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 骑手id
     */
    private Integer deliverymanId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 用户id
     */
    private Integer accountId;

    /**
     * 结算id
     */
    private Integer settlementId;

    /**
     * 积分奖励id
     */
    private Integer rewardId;

    /**
     * 收到的数量
     */
    private BigDecimal rewardAmount;

    /**
     * 确认标识
     */
    private Boolean confirmed;
}
