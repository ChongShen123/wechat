package com.xsdkj.wechat.service.impl;










import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.dto.UserCommentDto;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.mood.UserComment;
import com.xsdkj.wechat.mapper.UserCommentMapper;
import com.xsdkj.wechat.netty.cmd.base.BaseHandler;
import com.xsdkj.wechat.service.SingleChatService;
import com.xsdkj.wechat.service.UserCommentService;
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
public class UserCommentServiceImpl implements UserCommentService {
    @Resource
    private UserUtil userUtil;
    @Resource
    UserCommentMapper userCommentMapper;
    @Resource
    SingleChatService singleChatService;

    @Override
    public void save(UserCommentDto userCommentDto) {
        UserComment userComment = createUserComment(userCommentDto);
        userCommentMapper.insert(userComment);
        Integer userId = userUtil.currentUser().getUser().getId();
        List<Integer> ids=new ArrayList<>();
        List<UserFriendVo> userFriendVos = userUtil.currentUser().getUserFriendVos();
        userFriendVos.forEach(userFriendVo -> ids.add(userFriendVo.getUid()));
        if(ids.size()>0){
            for(Integer id :ids){
                SingleChat singleChat=new SingleChat();
                singleChat.setContent("用户"+userUtil.currentUser().getUsername()+"评论了");
                singleChat.setType(ChatConstant.USER_COMMENT);
                singleChat.setToUserId(id);
                singleChat.setFromUserId(userId);
                singleChat.setCreateTimes(System.currentTimeMillis());
                Channel channel = SessionUtil.ONLINE_USER_MAP.get(id);
                if(channel != null){
                    BaseHandler.sendMessage(channel, JsonResult.success(singleChat, Cmd.SINGLE_CHAT));
                    singleChat.setRead(true);
                }else{
                    singleChat.setRead(false);
                }
                singleChatService.save(singleChat);
            }
        }


        /*rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_COMMENT, userComment));*/

    }
    private UserComment createUserComment(UserCommentDto userCommentDto) {
        UserComment userComment = new UserComment();
        userComment.setUid(userUtil.currentUser().getUser().getId());
        userComment.setNickname(userCommentDto.getNickname());
        userComment.setContent(userCommentDto.getContent());
        userComment.setCreateTimes(System.currentTimeMillis());
        if (userCommentDto.getMoodId() != null) {
            userComment.setMoodId(userCommentDto.getMoodId());
        }
        return userComment;
    }

    @Override
    public void delete(UserComment userComment) {


    if(userComment.getId()!=null){
        userComment.setUid(userUtil.currentUser().getUser().getId());
        userCommentMapper.deleteByPrimaryKey(userComment.getId());
       /* rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_COMMENT,userComment));*/
    }

    }
}
