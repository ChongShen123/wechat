package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/15 14:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DataEmptyException extends ServiceException {
    private static final long serialVersionUID = -60463735350010518L;

    public DataEmptyException() {
        code = ResultCodeEnum.DATA_NOT_EXIST;
    }
}
