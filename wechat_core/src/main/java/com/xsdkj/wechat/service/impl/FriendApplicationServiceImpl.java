package com.xsdkj.wechat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.service.FriendApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Queue;

/**
 * @author tiankong
 * @date 2019/12/12 13:56
 */
@Slf4j
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
        long begin = System.currentTimeMillis();
        log.debug("检查{},{} 是否存在好友信息消息", toUserId, formUserId);
        Criteria criteria = new Criteria();
        criteria.and("toUserId").is(toUserId);
        criteria.and("fromUserId").is(formUserId);
        criteria.and("state").is(0);
        Query query = Query.query(criteria);
        long count = mongoTemplate.count(query, FriendApplication.class);
        log.debug("检查完毕:{} {}ms", count, DateUtil.spendMs(begin));
        return count;
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
        query.fields().exclude("modifiedTime").exclude("byteType").exclude("toUserId");
        return mongoTemplate.find(query, FriendApplication.class);
    }

    @Override
    public void updateFriendApplicationRead(boolean read, List<String> ids) {
        Criteria criteria = new Criteria();
        criteria.and("id").in(ids);
        Query query = Query.query(criteria);
        Update update = new Update();
        update.set("isRead", read);
        mongoTemplate.updateMulti(query, update, FriendApplication.class);
    }

    @Override
    public List<FriendApplication> listByReadAndUserId(boolean isRead, Integer uid) {
        Criteria criteria = new Criteria();
        criteria.and("isRead").is(isRead);
        criteria.and("toUserId").is(uid);
        return mongoTemplate.find(Query.query(criteria), FriendApplication.class);
    }
}
