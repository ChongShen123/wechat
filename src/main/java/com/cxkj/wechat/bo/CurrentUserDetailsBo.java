package com.cxkj.wechat.bo;

import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.vo.ListGroupVo;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/11 13:10
 */
@Data
public class CurrentUserDetailsBo implements UserDetails {
    private User user;
    /**
     * 用户的的菜单
     */
    private List<PermissionBo> permissionBos;
    /**
     * 用户的所有群组
     */
    private List<ListGroupVo> groupInfoBos;

    public CurrentUserDetailsBo(User user) {
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
