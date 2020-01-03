package com.xsdkj.wechat.service.impl;

import cn.hutool.core.io.FileUtil;
import com.xsdkj.wechat.bo.MsgBox;
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
    @Resource
    private RabbitTemplateService rabbitTemplateService;
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
    public void selectAll( ) {
        UserMood userMood=new UserMood();
        userMood.setUid(userUtil.currentUser().getUser().getId());
/*        userMoodMapper.selectAllMood(userMood);*/


    }

    @Override
    public List<UserMoodVo> listUserMoodByUid() {
        List<Integer> friendIds = new ArrayList<>();
        friendIds.add(userUtil.currentUser().getUser().getId());
        List<UserFriendVo> userFriendVos = userUtil.currentUser().getUserFriendVos();
        userFriendVos.forEach(userFriendVo -> friendIds.add(userFriendVo.getUid()));
        return userMoodMapper.listUserMoodByUid(friendIds);
    }
    @Override
    public void save(MoodParamDto moodDto) {
        String[] files = moodDto.getFile().split(",");
        if (files.length > 0) {
            for (String file : files) {
                boolean exist = FileUtil.exist(rootPath + imgPath + file);
                if (!exist) {
                    throw new FileNotFoundException(ResultCodeEnum.FILE_NOT_FUND);
                }
            }
        }
        UserMood userMood = createNewUserMood(moodDto);
        rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, MsgBox.create(SystemConstant.BOX_TYPE_MOOD, userMood));
    }
    @Override
    public void delete(UserMood userMood) {
        if (userMood.getId() != null) {
            userMood.setUid(userUtil.currentUser().getUser().getId());
            rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, MsgBox.create(SystemConstant.BOX_TYPE_MOOD, userMood));
        }
    }




    private UserMood createNewUserMood(MoodParamDto moodDto) {
        UserMood userMood = new UserMood();
        userMood.setContent(moodDto.getContent());
        userMood.setFile(moodDto.getFile());
        userMood.setFileType(moodDto.getFileType());
        userMood.setCreateTimes(System.currentTimeMillis());
        userMood.setUid(userUtil.currentUser().getUser().getId());
        return userMood;
    }
}
