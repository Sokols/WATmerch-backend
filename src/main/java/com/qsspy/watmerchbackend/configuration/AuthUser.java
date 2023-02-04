package com.qsspy.watmerchbackend.configuration;

import com.qsspy.watmerchbackend.entity.ShopUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class AuthUser implements UserDetails {

    private ShopUser shopUser;

    public AuthUser(ShopUser shopUser) {

        this.shopUser = shopUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(shopUser.getRole().getName().name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return shopUser.getPassword();
    }

    @Override
    public String getUsername() {
        return shopUser.getUsername();
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
        return shopUser.isEnabled();
    }
}
