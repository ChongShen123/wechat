package com.xsdkj.wechat.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/15 14:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupInfoVo extends GroupBaseInfoVo implements Serializable {
    private String icon;
    private String qr;
    private String notice;
    private Long createTimes;
}
