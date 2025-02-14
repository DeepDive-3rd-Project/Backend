package goorm.deepdive.team1.domain.admin.application;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.domain.Role;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import goorm.deepdive.team1.domain.admin.security.PasswordEncryptor;
import goorm.deepdive.team1.domain.admin.exception.AdminPasswordMismatchException;
import goorm.deepdive.team1.domain.admin.exception.AdminNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommandService {
    private final AdminRepository adminRepository;
    private final PasswordEncryptor passwordEncryptor;

    public Admin register(String email, String password, String role) {
        Admin admin = Admin.builder()
                .email(email)
                .password(passwordEncryptor.encode(password))
                .role(Role.valueOf(role))
                .build();
        return adminRepository.save(admin);
    }

    @Transactional
    public void deleteAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(AdminNotFoundException::new);

        adminRepository.delete(admin);
    }

    @Transactional
    public void updatePassword(Long adminId, String oldPassword, String newPassword) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(AdminNotFoundException::new);

        // 기존 비밀번호 검증
        if (!passwordEncryptor.matches(oldPassword, admin.getPassword())) {
            throw new AdminPasswordMismatchException();
        }

        // 새 비밀번호 암호화 후 저장
        admin.updatePassword(passwordEncryptor.encode(newPassword));
        adminRepository.save(admin);
    }

}