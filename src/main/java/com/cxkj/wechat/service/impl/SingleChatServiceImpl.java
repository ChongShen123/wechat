package com.cxkj.wechat.service.impl;

import cn.hutool.core.io.FileUtil;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.service.SingleChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    @Override
    public void deleteImage() {
        // step1 先查询 7天之前的所有数据。 List<SingleChat> list;
        Query query1=Query.query(Criteria.where("createTime").lt(time));
        List<SingleChat> list = mongoTemplate.find(query1, SingleChat.class);
        // step2 遍历这个list
        for (SingleChat singleChat : list) {
            Byte type = singleChat.getType();
            if (type == SystemConstant.CHAT_TYPE_VOICE || type == SystemConstant.CHAT_TYPE_IMG) {
              String path =  singleChat.getContent();
                if (path == null) {
                    continue;
                }
                File file=new File(rootPath+path);
                if (file.exists()) {
                    file.delete();
                }
            }
            //  step4 直接删除
           Query query = Query.query(Criteria.where("id").is(singleChat.getId()));
            mongoTemplate.remove(query,SingleChat.class);
        }
    }


}
