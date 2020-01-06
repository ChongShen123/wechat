package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/30 14:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileNotFoundException extends ServiceException implements Serializable {
    private static final long serialVersionUID = -2146716503601972045L;

    public FileNotFoundException() {
        code = ResultCodeEnum.FILE_NOT_FUND;
    }

}
