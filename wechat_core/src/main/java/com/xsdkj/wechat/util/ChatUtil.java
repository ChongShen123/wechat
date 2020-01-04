package com.xsdkj.wechat.util;

import cn.hutool.core.lang.Snowflake;
import com.xsdkj.wechat.entity.chat.SingleChat;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/3 13:55
 */
@Component
public class ChatUtil {
    @Resource
    private Snowflake snowflake;

    /**
     * 创建一条单聊消息
     *
     * @param toUserId   toUserId
     * @param fromUserId fromUserId
     * @param content    content
     * @param type       0信息 1语音 2图片 3撤销 4 加入群聊 5退群 6红包 7转账
     * @return SingleChat
     */
    public SingleChat createNewSingleChat(Integer toUserId, Integer fromUserId, String content, Byte type) {
        SingleChat chat = new SingleChat();
        chat.setId(snowflake.nextIdStr());
        chat.setContent(content);
        chat.setFromUserId(fromUserId);
        chat.setToUserId(toUserId);
        chat.setType(type);
        chat.setCreateTimes(System.currentTimeMillis());
        return chat;
    }
}
