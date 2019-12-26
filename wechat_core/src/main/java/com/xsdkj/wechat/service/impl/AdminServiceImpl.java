package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.bo.PermissionBo;
import com.xsdkj.wechat.mapper.UserMapper;
import com.xsdkj.wechat.service.AdminService;
import com.xsdkj.wechat.util.JwtTokenUtil;
import com.xsdkj.wechat.util.UserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/22 11:26
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private UserUtil userUtil;
    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public List<PermissionBo> getMenu() {
        List<PermissionBo> permissionList = userMapper.listPermissionByUid(7);
        return getTree(0, permissionList);
    }

    /**
     * 递归分类用户菜单
     *
     * @param pid         pid
     * @param permissions 菜单
     * @return list
     */
    private List<PermissionBo> getTree(Integer pid, List<PermissionBo> permissions) {
        List<PermissionBo> childList = new ArrayList<>();
        permissions.forEach(permissionBo -> {
            if (permissionBo.getPid().equals(pid)) {
                childList.add(permissionBo);
            }
        });
        childList.forEach(permissionBo -> permissionBo.setChildren(getTree(permissionBo.getId(), permissions)));
        childList.sort(order());
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }

    /**
     * 排序规则 根据sort进行排序
     *
     * @return Comparator
     */
    private Comparator<PermissionBo> order() {
        return Comparator.comparingInt(PermissionBo::getSort);
    }
}
