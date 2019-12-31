package com.xsdkj.wechat.service;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/31 12:39
 */
public interface FriendApplicationService {
    /**
     * 更新好友申请消息为已读状态
     *
     * @param read 状态
     * @param ids  ids
     */
    void updateFriendApplicationRead(boolean read, List<String> ids);
}
