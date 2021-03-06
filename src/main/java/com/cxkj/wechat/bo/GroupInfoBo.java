package com.cxkj.wechat.bo;

import com.cxkj.wechat.entity.Group;
import io.netty.channel.group.ChannelGroup;
import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/12 10:11
 */
@Data
public class GroupInfoBo {
    private Group group;
    private ChannelGroup channelGroup;

    public GroupInfoBo(Group group, ChannelGroup channelGroup) {
        this.group = group;
        this.channelGroup = channelGroup;
    }
}
