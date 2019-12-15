package com.cxkj.wechat.vo;

import com.cxkj.wechat.entity.Group;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/14 15:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateGroupVO extends ChatResponse {
    private Integer id;
    private String name;
    private Integer memberCount;
    private String icon;
    private Long createTimes;

    public CreateGroupVO(Group group) {
        id = group.getId();
        name = group.getName();
        icon = group.getIcon();
        memberCount = group.getMembersCount();
        createTimes = group.getCreateTimes();
    }
}
