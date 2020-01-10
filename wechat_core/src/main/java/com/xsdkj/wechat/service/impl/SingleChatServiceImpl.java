package com.xsdkj.wechat.service.impl;


import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.service.SingleChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class SingleChatServiceImpl implements SingleChatService {
    /**
     * TODO 这里文件路径有问题需要修改
     */

    @Value("${file.single-chat}")
    private String singleChatFile;
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * //获取当前时间的前15天的时间戳 TODO 修改需要下不能这样写
     */
    Long time = System.currentTimeMillis() - 1296000000;

    @Override
    public void save(SingleChat singleChat) {
        mongoTemplate.save(singleChat);
    }
    /*
    定时器删除数据库存储时间超过7天的数据
    step:1 查询 7天之前人所有数据  条件:
    step:2 执行 删除。
     */


    @Override
    public void deleteSingleChat() {
        // step1 先查询 7天之前的所有数据。 List<SingleChat> list;
        Query query1 = Query.query(Criteria.where("createTimes").lt(time));
        List<SingleChat> list = mongoTemplate.find(query1, SingleChat.class);
        if (list.size() > 0) {
            // step2 遍历这个list
            for (SingleChat singleChat : list) {
                Byte type = singleChat.getType();
                if (type == ChatConstant.CHAT_TYPE_VOICE || type == ChatConstant.CHAT_TYPE_IMG) {
                    String path = singleChat.getContent();
                    if (path == null) {
                        continue;
                    }
                    File file = new File(singleChatFile + path);
                    if (file.exists()) {
                        file.delete();
                    } else {
                        log.error("{}文件不存在", file.getName());
                    }
                }
            }
            mongoTemplate.findAllAndRemove(query1, SingleChat.class);
        }
    }


    @Override
    public SingleChat getById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, SingleChat.class);
    }

    @Override
    public void deleteById(String id) {
        long begin = System.currentTimeMillis();
        Query query = Query.query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, SingleChat.class);
        log.debug("已删除ID:{}的单聊消息 {}ms", id, DateUtil.spendMs(begin));
    }

    @Override
    public void updateRead(boolean read, List<SingleChat> singleChats) {
        List<String> ids = new ArrayList<>();
        singleChats.forEach(item -> ids.add(item.getId()));
        Criteria criteria = new Criteria();
        criteria.and("id").in(ids);
        Query query = Query.query(criteria);
        Update update = new Update();
        update.set("read", true);
        mongoTemplate.updateMulti(query, update, SingleChat.class);
    }

    @Override
    public List<SingleChat> listByReadAndToUserId(boolean read, Integer toUserId) {
        Criteria criteria = new Criteria();
        criteria.and("read").is(read);
        criteria.and("toUserId").is(toUserId);
        return mongoTemplate.find(Query.query(criteria), SingleChat.class);
    }
}
