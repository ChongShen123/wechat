package com.xsdkj.wechat.service;


import com.xsdkj.wechat.entity.chat.GroupChat;

/**
 * 保存 删除群聊消息
 */
public interface GroupChatService {
    void save(GroupChat groupChat);


    void deleteGroupChat();
}
