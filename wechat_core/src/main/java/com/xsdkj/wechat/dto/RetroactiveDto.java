package com.xsdkj.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author tiankong
 * @date 2020/1/6 10:45
 */
@Data
public class RetroactiveDto {
    @NotNull(message = "补签日期不能为空")
    private Date date;
}
