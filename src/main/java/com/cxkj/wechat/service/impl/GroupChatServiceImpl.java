package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.FriendApplication;
import com.cxkj.wechat.entity.GroupChat;
import com.cxkj.wechat.service.GroupChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.File;
import java.util.List;


@Service
@Slf4j
public class GroupChatServiceImpl implements GroupChatService {

    @Value("${file.group-chat}")
    private String groupChatFile;


    @Resource
    private MongoTemplate mongoTemplate;
//获取当前时间的前15天的时间戳
    Long time = System.currentTimeMillis() - 1296000000;

    /**
     * 保存群聊
     *
     * @param groupChat
     */
    @Override
    public void save(GroupChat groupChat) {
        GroupChat groupChat1 = new GroupChat();
        groupChat1.setToGroupId(groupChat.getToGroupId());
        groupChat1.setId(groupChat.getId());
        groupChat1.setFromUserId(groupChat.getFromUserId());
        groupChat1.setContent(groupChat.getContent());
        groupChat1.setType(groupChat.getType());
        groupChat1.setCreateTimes(groupChat.getCreateTimes());
        mongoTemplate.save(groupChat1);
    }


    /**
     * 删除群聊消息
     */

    @Override
    public void deleteGroupChat() {
        /**
         * 获取过期的时间的
         */
        Query createTimes = Query.query(Criteria.where("createTimes").lt(time));
        List<GroupChat> groupChats = mongoTemplate.find(createTimes, GroupChat.class);
        System.out.println(groupChats.size());
//        /**
//         * 遍历查询到的储存时间到期的图片和语音
//         */
        if (groupChats.size() > 0) {
            for (GroupChat groupChat : groupChats) {
                Byte type = groupChat.getType();
                if (type == SystemConstant.CHAT_TYPE_VOICE || type == SystemConstant.CHAT_TYPE_IMG) {
                    String path = groupChat.getContent();
                    if (path == null) {
                        continue;
                    }
                    String realName = groupChatFile + path;
                    File file = new File(realName);
                    if (file.exists()) {
                        file.delete();
                    } else {
                        log.error("{}文件不存在", file.getName());
                        continue;
                    }
                }
            }
            mongoTemplate.findAllAndRemove(createTimes, GroupChat.class);
        }
    }
}
