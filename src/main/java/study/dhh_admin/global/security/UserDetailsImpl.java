package study.dhh_admin.global.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import study.dhh_admin.domain.owner.entity.Owner;

import java.util.Collection;


public class UserDetailsImpl implements UserDetails {
    private final Owner owner;

    public UserDetailsImpl(Owner user) {
        this.owner = user;
    }

    public Owner getUser() {
        return owner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return owner.getPassword();
    }

    @Override
    public String getUsername() {
        return owner.getEmail();
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
        return true;
    }
}