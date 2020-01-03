package com.xsdkj.wechat.entity.other;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/2 17:51
 */
@Data
public class NicknameName implements Serializable {
    private Integer id;

    private String surname;

    /**
    * 0:30,40 1:50,60 2:80,90 3:00,10 4:古代雅韵
    */
    private Byte type;

    private static final long serialVersionUID = 1L;
}
