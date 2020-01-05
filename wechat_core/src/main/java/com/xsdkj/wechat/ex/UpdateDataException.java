package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 修改数据异常
 *
 * @author tiankong
 * @date 2020/1/5 18:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateDataException extends ServiceException {
    private static final long serialVersionUID = 1997573866938675382L;

    public UpdateDataException() {
        code = ResultCodeEnum.UPDATE_DATE_EXCEPTION;
    }
}
