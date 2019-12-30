package com.xsdkj.wechat.entity.chat;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/28 15:54
 */
@Data
public class GroupNoSay implements Serializable {
    private Integer id;

    private Integer uid;

    private Integer groupId;

    /**
     * 禁言类型 -1 永久禁言, 当前时间戳之内则为正常发言,之外则为禁言时间
     */
    private Long times;

    private static final long serialVersionUID = 1L;
}