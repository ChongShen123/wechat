package com.xsdkj.wechat.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/15 13:57
 */
@Data
public class GroupBaseInfoVo implements Serializable {
    private static final long serialVersionUID = 3977934267788058659L;
    protected Integer id;
    protected String name;
    protected Integer membersCount;
    protected Boolean addFriendType;
    protected Byte noSayType;
}
