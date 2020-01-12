package com.xsdkj.wechat.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserWalletDto {
    Integer id;
    Integer uid;
    String username;
    BigDecimal price;
    BigDecimal totalPrice;
    Long modifiedTimes;
    Byte state;
    Integer pageNum;
    Integer pageSize;

}
