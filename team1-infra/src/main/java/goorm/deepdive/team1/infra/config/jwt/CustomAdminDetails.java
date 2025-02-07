package goorm.deepdive.team1.infra.config.jwt;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomAdminDetails implements UserDetails {

    private final Admin admin;

    public CustomAdminDetails(Admin admin) {
        this.admin = admin;
    }

    public Admin getAdmin() {
        return admin;
    }

    public Long getAdminId() {
        return admin.getId();
    }

    @Override
    public String getUsername() {
        return admin.getEmail();
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> admin.getRole().name()); // 람다식을 사용하여 GrantedAuthority 구현
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        // 관리자의 활성 상태를 항상 true로 설정 (추후 필요시 조건 추가 가능)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 인증 정보가 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않음
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않음
    }
}