package com.example.customuser2.domain;

import com.example.customuser2.domain.type.UserGrade;
import com.example.customuser2.entity.user.Privilege;
import com.example.customuser2.entity.user.Role;
import com.example.customuser2.entity.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// domain layer는 특정 기술에 종속되지 않아야하므로 UserDetails를 상속
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // https://www.baeldung.com/role-and-privilege-for-spring-security-registration
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(AuthorityUtils.createAuthorityList(getPrivileges(user.getRoles())));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege privilege : collection) {
            privileges.add(privilege.getName());
        }
        return privileges;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return user.getEnabled().isStatus();
    }

    public UserGrade getUserGrade() {
        return user.getUserGrade();
    }
}
