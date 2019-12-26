package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.service.FriendApplicationService;
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
        Criteria criteria = new Criteria();
        criteria.and("toUserId").is(toUserId);
        criteria.and("fromUserId").is(formUserId);
        criteria.and("state").is(0);
        Query query = Query.query(criteria);
        return mongoTemplate.count(query, FriendApplication.class);
    }

    @Override
    public FriendApplication getFriendApplicationById(String id) {
        return mongoTemplate.findById(id, FriendApplication.class);
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
