package com.cxkj.wechat.service.impl;

import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.service.SingleChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;



@Service
public class SingleChatServiceImpl implements SingleChatService {
    @Value("${file.root-path}")
    private String rootPath;
    @Value("${file.img-path}")
    private String imgPath;
    @Resource
    private MongoTemplate mongoTemplate;

    Long time = System.currentTimeMillis()-180000;
    SingleChat single=new SingleChat();
    @Override
    public void save(SingleChat singleChat) {

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
    step:1 查询 7天之前人所有数据  条件:
    step:2 执行 删除。
     */
    //604800000
   @Override
    public void deleteTask() {
        Query query =Query.query(Criteria.where("createTimes").lt(time));
        mongoTemplate.remove(query,SingleChat.class);
    }
    /**
     * 删除数据库里存储的过期图片地址
     */
@Override
    public void deleteImage() {
        Query type = Query.query(Criteria.where("type").lt(SystemConstant.CHAT_TYPE_IMG));



    }


}
