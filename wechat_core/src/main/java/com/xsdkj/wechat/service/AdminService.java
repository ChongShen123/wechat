package com.xsdkj.wechat.service;


import com.xsdkj.wechat.bo.PermissionBo;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/22 10:59
 */
public interface AdminService {

    /**
     * 获取用户菜单
     *
     * @return list
     */
    List<PermissionBo> getMenu();
}
