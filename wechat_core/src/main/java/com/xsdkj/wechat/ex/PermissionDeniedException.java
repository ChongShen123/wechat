package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.IErrorCode;
import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/26 18:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionDeniedException extends ServiceException {
    private static final long serialVersionUID = 2032976030274369402L;

    private IErrorCode code = ResultCodeEnum.FORBIDDEN;


    public PermissionDeniedException() {
        code = ResultCodeEnum.FORBIDDEN;
    }
}
