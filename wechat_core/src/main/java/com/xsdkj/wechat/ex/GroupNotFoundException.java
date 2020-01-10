package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/15 19:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupNotFoundException extends ServiceException {
    private static final long serialVersionUID = -4654107370573235519L;

    public GroupNotFoundException() {
        code = ResultCodeEnum.GROUP_NOT_FOUND;
    }
}
