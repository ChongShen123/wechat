package com.xsdkj.wechat.service.ex;

import com.xsdkj.wechat.common.IErrorCode;
import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/15 14:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DataEmptyException extends RuntimeException {
    private static final long serialVersionUID = -60463735350010518L;
    private IErrorCode code = ResultCodeEnum.DATA_NOT_EXIST;

    public DataEmptyException() {
        super();
    }

}
