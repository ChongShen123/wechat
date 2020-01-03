package com.xsdkj.wechat.bo;

import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.vo.GroupVo;
import com.xsdkj.wechat.vo.UserFriendVo;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tiankong
 * @date 2019/12/11 13:10
 */
@Data
public class UserDetailsBo implements UserDetails, Serializable {
    private static final long serialVersionUID = -3880140593105583167L;

    /**
     * 此无参构造器不能删除 否则 JSONObject 会解析失败
     */
    public UserDetailsBo() {
    }

    private User user;
    /**
     * 用户钱包
     */
    private Wallet wallet;
    /**
     * 用户的的菜单
     */
    private List<PermissionBo> permissionBos;
    /**
     * 用户的所有群组
     * TODO 这里可以去掉.与下面的Map重复了
     */
    private List<GroupVo> groupInfoBos;
    /**
     * 用户所有好友
     */
    private List<UserFriendVo> userFriendVos;
    /**
     * 用户所有群组
     */
    private Map<Integer, GroupVo> userGroupRelationMap = new HashMap<>();

    public UserDetailsBo(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getState() != 1;
    }
}
