package com.xsdkj.wechat.dto;

import com.xsdkj.wechat.ex.ValidateException;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/12 18:48
 */
@Data
public class UpdateSignDateDto {
    private Integer retroactiveTimeFrame;
    private Integer maxRetroactiveCount;

    public void setRetroactiveTimeFrame(Integer retroactiveTimeFrame) {
        if (retroactiveTimeFrame != null && retroactiveTimeFrame < 0) {
            throw new ValidateException();
        }
        this.retroactiveTimeFrame = retroactiveTimeFrame;
    }

    public void setMaxRetroactiveCount(Integer maxRetroactiveCount) {
        if (maxRetroactiveCount != null && maxRetroactiveCount < 1) {
            throw new ValidateException();
        }
        this.maxRetroactiveCount = maxRetroactiveCount;
    }

}
