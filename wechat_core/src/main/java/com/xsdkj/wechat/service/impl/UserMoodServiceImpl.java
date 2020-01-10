package com.xsdkj.wechat.service.impl;

import cn.hutool.core.io.FileUtil;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.dto.MoodParamDto;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.mapper.UserMoodMapper;
import com.xsdkj.wechat.entity.mood.UserMood;
import com.xsdkj.wechat.ex.FileNotFoundException;
import com.xsdkj.wechat.netty.cmd.base.BaseHandler;
import com.xsdkj.wechat.service.RabbitTemplateService;
import com.xsdkj.wechat.service.SingleChatService;
import com.xsdkj.wechat.service.UserMoodService;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.util.UserUtil;
import com.xsdkj.wechat.vo.UserFriendVo;
import com.xsdkj.wechat.vo.UserMoodVo;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Administrator
 */
@Service
public class UserMoodServiceImpl implements UserMoodService {
    @Resource
    private UserUtil userUtil;
    @Value("${file.root-path}")
    private String rootPath;
    @Value("${file.img-path}")
    private String imgPath;
    @Resource
    UserMoodMapper userMoodMapper;
    @Resource
    RabbitTemplateService rabbitTemplateService;
    @Resource
    private SingleChatService singleChatService;

    /**
     * 查询好友的动态
     */
    @Override
    public List<UserMoodVo> listUserMoodByUid() {
        int id = userUtil.currentUser().getUser().getId();
        List<UserFriendVo> userFriendVos = userUtil.currentUser().getUserFriendVos();
        List<Integer> ids = new ArrayList<>();
        userFriendVos.forEach(userFriendVo -> ids.add(userFriendVo.getUid()));
        ids.add(id);
        return userMoodMapper.listUserMoodByUid(ids);
    }
    @Override
    public void save(MoodParamDto moodDto) {
        if (moodDto.getFile() != null) {
            String[] files = moodDto.getFile().split(",");
            if (files.length > 0) {
                for (String file : files) {
                    boolean exist = FileUtil.exist(rootPath + imgPath + file);
                    if (!exist) {
                        throw new FileNotFoundException();
                    }
                }
            }
        }
        UserMood userMood = createNewUserMood(moodDto);
        userMoodMapper.insert(userMood);
        int userId = userUtil.currentUser().getUser().getId();
        List<Integer> ids = new ArrayList<>();
        // 找到用户所有好友 id
        List<UserFriendVo> userFriendVos = userUtil.currentUser().getUserFriendVos();
        userFriendVos.forEach(userFriendVo -> ids.add(userFriendVo.getUid()));
        if (ids.size() > 0) {
            for (Integer id : ids) {
                SingleChat singleChat = new SingleChat();
                singleChat.setContent("用户"+userUtil.currentUser().getUsername()+"发标了新的动态");
                singleChat.setType(ChatConstant.SEND_MOOD);
                singleChat.setToUserId(id);
                singleChat.setFromUserId(userId);
                singleChat.setCreateTimes(System.currentTimeMillis());
                Channel channel = SessionUtil.ONLINE_USER_MAP.get(id);
                if (channel != null) {
                    BaseHandler.sendMessage(channel, JsonResult.success(singleChat, Cmd.SINGLE_CHAT));
                    singleChat.setRead(true);
                }else{
                    singleChat.setRead(false);
                }
                singleChatService.save(singleChat);
            }
        }
        /*rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, MsgBox.create(SystemConstant.BOX_TYPE_MOOD,userMood));*/
    }
    @Override
    public void delete(UserMood userMood) {
        if (userMood.getId() != null) {
            userMood.setUid(userUtil.currentUser().getUser().getId());
            userMoodMapper.deleteByPrimaryKey(userMood.getId());
          /*  rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_MOOD, userMood));*/


        }
    }

    private UserMood createNewUserMood(MoodParamDto moodDto) {
        UserMood userMood = new UserMood();
        userMood.setContent(moodDto.getContent());
        if (moodDto.getFile() != null) {
            userMood.setFile(moodDto.getFile());
            userMood.setFileType(moodDto.getFileType());
        }
        userMood.setCreateTimes(System.currentTimeMillis());
        userMood.setUid(userUtil.currentUser().getUser().getId());
        return userMood;
    }
}
