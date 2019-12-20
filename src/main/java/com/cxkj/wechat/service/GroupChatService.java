package com.cxkj.wechat.service;

import com.cxkj.wechat.entity.GroupChat;

/**
 * 保存 删除群聊消息
 */
public interface GroupChatService {
    void save (GroupChat groupChat);

    void deleteGroup();

    void deleteGroupImage();
}
