package me.erudev.order.enums;

/**
 * @author pengfei.zhao
 * @date 2021/2/11 13:10
 */
public enum OrderStatus {
    /** 订单创建中 */
    ORDER_CREATING,
    /** 商家确认 */
    RESTAURANT_CONFIRMED,
    /** 骑手确认 */
    DELIVERYMAN_CONFIRMED,
    /** 订单结算确认 */
    SETTLEMENT_CONFIRMED,
    /** 创建订单 */
    ORDER_CREATED,
    /** 订单创建失败 */
    FAILED;
}
