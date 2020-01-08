package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.UserThumbsDto;
import com.xsdkj.wechat.entity.mood.UserThumbs;

/**
 * @author
 */
public interface UserThumbsService {
    void save(UserThumbsDto userThumbsDto);
    void delete();
}
