package com.xsdkj.wechat.entity.other;

import java.io.Serializable;
import lombok.Data;

/**
 * @author tiankong
 * @date 2020/1/2 17:51
 */
@Data
public class NicknameSurname implements Serializable {
    private Integer id;

    private String surname;

    /**
    * 1 百家姓
2 网络
    */
    private Byte type;

    private static final long serialVersionUID = 1L;
}
