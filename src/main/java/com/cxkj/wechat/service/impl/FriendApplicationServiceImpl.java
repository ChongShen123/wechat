package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.entity.FriendApplication;
import com.cxkj.wechat.service.FriendApplicationService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 13:56
 */
@Service
public class FriendApplicationServiceImpl implements FriendApplicationService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void save(FriendApplication application) {
        mongoTemplate.save(application);
    }

    @Override
    public Long countByToUserIdAndFromUserId(Integer toUserId, Integer formUserId) {
        Query query = Query.query(Criteria.where("toUserId").is(toUserId).andOperator(Criteria.where("fromUserId").is(formUserId)));
        return mongoTemplate.count(query, FriendApplication.class);
    }

    @Override
    public FriendApplication getFriendApplicationById(String id) {
        return mongoTemplate.findById(new ObjectId(id), FriendApplication.class);
    }

    @Override
    public void update(FriendApplication application) {
        Query query = Query.query(Criteria.where("id").is(application.getId()));
        Update update = Update.update("state", application.getState()).set("modifiedTime", application.getModifiedTime()).set("isRead", application.isRead());
        mongoTemplate.updateFirst(query, update, FriendApplication.class);
    }

    @Override
    public void deleteById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        if (query.getLimit() != 0) {
            mongoTemplate.remove(query, FriendApplication.class);
        }
    }

    @Override
    public List<FriendApplication> listByUserId(Integer userId) {
        Query query = Query.query(Criteria.where("toUserId").is(userId));
        query.fields().exclude("modifiedTime").exclude("type").exclude("toUserId");
        return mongoTemplate.find(query, FriendApplication.class);
    }
}
