package com.xsdkj.wechat.vo.admin;

import com.xsdkj.wechat.bo.PermissionBo;
import lombok.Data;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/23 10:06
 */
@Data
public class LoginInfoVo {
    private String name;
    private String avatar;
    private List<PermissionBo> menus;
}
