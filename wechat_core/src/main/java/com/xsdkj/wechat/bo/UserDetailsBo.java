package com.xsdkj.wechat.bo;

import com.xsdkj.wechat.entity.chat.User;
import com.xsdkj.wechat.vo.ListGroupVo;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/11 13:10
 */
@Data
public class UserDetailsBo implements UserDetails, Serializable {
    /**
     * 此无参构造器不能删除 否则 JSONObject 会解析失败
     */
    public UserDetailsBo() {
    }

    private User user;
    /**
     * 用户的的菜单
     */
    private List<PermissionBo> permissionBos;
    /**
     * 用户的所有群组
     */
    private List<ListGroupVo> groupInfoBos;

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
