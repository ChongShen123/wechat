package com.xsdkj.wechat.bo;

import com.xsdkj.wechat.entity.chat.UserGroup;
import io.netty.channel.group.ChannelGroup;
import lombok.Data;

/**
 * @author tiankong
 * @date 2019/12/12 10:11
 */
@Data
public class GroupInfoBo {
    private UserGroup group;
    private ChannelGroup channelGroup;

    public GroupInfoBo(UserGroup group, ChannelGroup channelGroup) {
        this.group = group;
        this.channelGroup = channelGroup;
    }
}
