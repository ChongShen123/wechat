package com.cxkj.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/15 13:57
 */
@Data
public class GroupBaseInfoVO implements Serializable {
    protected Integer id;
    protected String name;
    protected Integer membersCount;
}
