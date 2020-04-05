package com.gmall.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-15 20:07:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Integer skuId;
    private Integer memberId;
    private String userName;
    private String password;
}
