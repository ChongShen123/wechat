package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.service.FriendApplicationService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/31 12:41
 */
@Service
public class FriendApplicationServiceImpl implements FriendApplicationService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void updateFriendApplicationRead(boolean read, List<String> ids) {
        Criteria criteria = new Criteria();
        criteria.and("id").in(ids);
        Query query = Query.query(criteria);
        Update update = new Update();
        update.set("isRead", read);
        mongoTemplate.updateMulti(query, update, FriendApplication.class);
    }
}
