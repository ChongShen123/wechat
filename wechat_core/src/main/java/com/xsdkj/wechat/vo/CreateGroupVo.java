package com.xsdkj.wechat.vo;

import com.xsdkj.wechat.entity.chat.UserGroup;
import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/14 15:34
 */
@Data
public class CreateGroupVo  {
    private Integer id;
    private String name;
    private Integer memberCount;
    private String icon;
    private Long createTimes;

    public CreateGroupVo(UserGroup group) {
        id = group.getId();
        name = group.getName();
        icon = group.getIcon();
        memberCount = group.getMembersCount();
        createTimes = group.getCreateTimes();
    }
}
