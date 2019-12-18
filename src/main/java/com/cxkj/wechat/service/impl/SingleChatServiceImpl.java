package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.service.SingleChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


@Service
public class SingleChatServiceImpl implements SingleChatService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(SingleChat singleChat) {
        SingleChat single = new SingleChat();
        single.setId(singleChat.getId());
        single.setCreateTimes(singleChat.getCreateTimes());
        single.setType(singleChat.getType());
        single.setRead(singleChat.getRead());
        single.setFromUserId(singleChat.getFromUserId());
        single.setContent(singleChat.getContent());
        single.setToUserId(singleChat.getToUserId());
        mongoTemplate.save(single);
    }

    /*
    定时器删除数据库存储时间超过7天的数据
    step:1 查询 7天之前人所有数据  条件 ：
    step:2 执行 删除。
     */
    @Override //604800000
    public void deleteTask(SingleChat singleChat) {
        Long time = System.currentTimeMillis() - 180000;
        Query query = Query.query(Criteria.where("createTimes").lt(time));
        mongoTemplate.remove(query, SingleChat.class);


    }

    @Override
    public SingleChat getById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, SingleChat.class);
    }

    @Override
    public void deleteById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, SingleChat.class);
    }
}
