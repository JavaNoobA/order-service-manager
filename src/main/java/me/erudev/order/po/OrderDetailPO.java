package me.erudev.order.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.erudev.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;

/**
 * order_detail 对应的实体
 *
 * @author pengfei.zhao
 * @date 2021/2/11 13:09
 */
@Getter
@Setter
@ToString
public class OrderDetailPO {
    /**
     * 订单id
     */
    private Integer id;

    /**
     * 订单状态
     */
    private OrderStatus status;

    /**
     * 地址
     */
    private String address;

    /**
     * 用户id
     */
    private Integer accountId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 骑手id
     */
    private Integer deliverymanId;

    /**
     * 结算id
     */
    private Integer settlementId;

    /**
     * 积分奖励id
     */
    private Integer rewardId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    private Date date;
}
