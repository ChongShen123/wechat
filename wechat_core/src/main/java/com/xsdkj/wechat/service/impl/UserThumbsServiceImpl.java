package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.dto.UserThumbsDto;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.mood.UserThumbs;
import com.xsdkj.wechat.mapper.UserThumbsMapper;
import com.xsdkj.wechat.netty.cmd.base.BaseHandler;
import com.xsdkj.wechat.service.SingleChatService;
import com.xsdkj.wechat.service.UserThumbsService;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.util.UserUtil;
import com.xsdkj.wechat.vo.UserFriendVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Administrator
 */
@Service
public class UserThumbsServiceImpl implements UserThumbsService {
    @Resource
    private UserUtil userUtil;
    @Resource
    UserThumbsMapper userThumbsMapper;
    @Resource
    SingleChatService singleChatService;
    /**
     * 保存用户点赞
     * @param userThumbsDto
     */
    @Override
    public void save(UserThumbsDto userThumbsDto) {
        UserThumbs userThumbs = saveThums(userThumbsDto);
        userThumbsMapper.insert(userThumbs);
        Integer userId = userUtil.currentUser().getUser().getId();
        List<Integer> ids=new ArrayList<>();
        List<UserFriendVo> userFriendVos = userUtil.currentUser().getUserFriendVos();
        userFriendVos.forEach(userFriendVo -> ids.add(userFriendVo.getUid()));
        if(ids.size()>0){
            for(Integer id :ids){
                SingleChat singleChat=new SingleChat();
                singleChat.setContent("用户"+userUtil.currentUser().getUsername()+"点赞了");
                singleChat.setType(ChatConstant.USER_THUMBS);
                singleChat.setToUserId(id);
                singleChat.setFromUserId(userId);
                singleChat.setCreateTimes(System.currentTimeMillis());
                Channel channel = SessionUtil.ONLINE_USER_MAP.get(id);
                if(channel !=null){
                    BaseHandler.sendMessage(channel, JsonResult.success(singleChat, Cmd.SINGLE_CHAT));
                    singleChat.setRead(true);
                }else{
                    singleChat.setRead(false);
                }
                singleChatService.save(singleChat);
            }
        }
    }
    private UserThumbs saveThums(UserThumbsDto userThumbsDto) {
        UserThumbs userThumbs = new UserThumbs();
        userThumbs.setUid(userUtil.currentUser().getUser().getId());
        userThumbs.setNickname(userThumbsDto.getNickname());
        if (userThumbsDto.getMoodId() != null) {
            userThumbs.setMoodId(userThumbsDto.getMoodId());
        }
        return userThumbs;
    }

    /**
     * 删除用户的点赞
     *
     * @param
     */
    @Override
    public void delete() {
        int id=userUtil.currentUser().getUser().getId();
        UserThumbs userThumbs=new UserThumbs();
        if (id==userThumbs.getId()) {
            userThumbsMapper.deleteByPrimaryKey(id);
            /*   rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME,RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_THUMS,userThumbs));*/
        }
    }

}
