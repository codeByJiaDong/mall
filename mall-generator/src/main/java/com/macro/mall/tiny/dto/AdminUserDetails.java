package com.macro.mall.tiny.dto;


import com.macro.mall.tiny.mbg.model.UmsAdmin;
import com.macro.mall.tiny.mbg.model.UmsPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zjd
 * @Description
 * @date 2020/4/1 15:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDetails implements UserDetails {
    //用户信息
    private UmsAdmin umsAdmin;
    //用户权限信息
    private List<UmsPermission> permissionList;

    /**
     * 返回当前用户的权限
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissionList.stream()
                .filter(permission -> permission.getValue()!=null)
                .map(permission -> new SimpleGrantedAuthority(permission.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return umsAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return umsAdmin.getUsername();
    }

    //是账户没有过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //是账户没加锁
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //证书没有过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //账户是否激活
    @Override
    public boolean isEnabled() {
        return umsAdmin.getStatus().equals(1);
    }
}
