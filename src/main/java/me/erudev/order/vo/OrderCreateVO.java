package me.erudev.order.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author pengfei.zhao
 * @date 2021/2/11 13:07
 */
@Getter
@Setter
@ToString
public class OrderCreateVO {
    /**
     * 用户id
     */
    private Integer accountId;

    /**
     * 地址
     */
    private String address;

    /**
     * 商品id
     */
    private Integer productId;
}
