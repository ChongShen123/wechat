package com.xsdkj.wechat.entity.user;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/3 9:45
 */
@Data
public class UserOperationLog implements Serializable {
    private Integer id;

    private Integer uid;

    /**
    * 平台id
    */
    private Integer platfromId;

    /**
    * 1 上分,2 下分,3 禁言,4 设置群可加好友 5 加签到次数,6 减签到次数 ...
    */
    private Byte operationType;

    /**
    * 描述操作内容
    */
    private String content;

    private Long createTimes;

    private static final long serialVersionUID = 1L;
}
