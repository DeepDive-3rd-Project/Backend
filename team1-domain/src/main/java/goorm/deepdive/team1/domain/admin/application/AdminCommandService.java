package goorm.deepdive.team1.domain.admin.application;


import goorm.deepdive.team1.common.exception.AdminExceptionCode;
import goorm.deepdive.team1.common.exception.CustomException;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.domain.Role;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import goorm.deepdive.team1.domain.admin.security.JwtTokenProvider;
import goorm.deepdive.team1.domain.admin.security.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommandService {
    private final AdminRepository adminRepository;
    private final PasswordEncryptor passwordEncryptor;
    private final JwtTokenProvider jwtTokenProvider;

    public Admin register(String email, String password, String role) {
        Admin admin = Admin.builder()
                .email(email)
                .password(passwordEncryptor.encode(password))
                .role(Role.valueOf(role))
                .build();
        return adminRepository.save(admin);
    }

    public String login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(AdminExceptionCode.ADMIN_NOT_FOUND));

        if (!passwordEncryptor.matches(password, admin.getPassword())) {
            throw new CustomException(AdminExceptionCode.NOT_ADMIN);
        }

        return jwtTokenProvider.generateToken(admin.getEmail(), admin.getRole().name());
    }

}