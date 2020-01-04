package com.xsdkj.wechat.service.impl;

import cn.hutool.core.io.FileUtil;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.dto.MoodParamDto;


import com.xsdkj.wechat.mapper.UserMoodMapper;

import com.xsdkj.wechat.entity.mood.UserMood;

import com.xsdkj.wechat.service.RabbitTemplateService;
import com.xsdkj.wechat.service.UserMoodService;
import com.xsdkj.wechat.service.ex.FileNotFoundException;
import com.xsdkj.wechat.util.UserUtil;
import com.xsdkj.wechat.vo.UserFriendVo;
import com.xsdkj.wechat.vo.UserMoodVo;
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
    /**
     * 查询好友的动态
     */

    @Override
    public List<UserMoodVo> listUserMoodByUid( ) {
       int id= userUtil.currentUser().getUser().getId();
        List<Integer> friendIds = new ArrayList<>();
        friendIds.add(id);
        List<UserFriendVo> userFriendVos = userUtil.currentUser().getUserFriendVos();
        for (UserFriendVo userFriendVo : userFriendVos){
            friendIds.add(userFriendVo.getUid());
        }
        return userMoodMapper.listUserMoodByUid(id);
    }

    @Override
    public void save(MoodParamDto moodDto) {
        if(moodDto.getFile()!=null){
            String[] files = moodDto.getFile().split(",");
            if (files.length > 0) {
                for (String file : files) {
                    boolean exist = FileUtil.exist(rootPath + imgPath + file);
                    if (!exist) {
                        throw new FileNotFoundException(ResultCodeEnum.FILE_NOT_FUND);
                    }
                }
            }
        }
        UserMood userMood = createNewUserMood(moodDto);
        userMoodMapper.insert(userMood);
        /*rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_MOOD, userMood));*/
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
        if(moodDto.getFile()!=null){
            userMood.setFile(moodDto.getFile());
            userMood.setFileType(moodDto.getFileType());
        }
        userMood.setCreateTimes(System.currentTimeMillis());
        userMood.setUid(userUtil.currentUser().getUser().getId());
        return userMood;
    }
}
