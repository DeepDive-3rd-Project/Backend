package goorm.deepdive.team1.domain.admin.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.domain.Role;
import goorm.deepdive.team1.domain.admin.exception.AdminNotFoundException;
import goorm.deepdive.team1.domain.admin.exception.AdminPasswordMismatchException;
import goorm.deepdive.team1.domain.admin.exception.CannotChangeOwnRoleException;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import goorm.deepdive.team1.domain.admin.security.PasswordEncryptor;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommandService {
    private final AdminRepository adminRepository;
    private final PasswordEncryptor passwordEncryptor;

    public Admin register(String email, String password, String role) {
        Admin admin = Admin.create(email, passwordEncryptor.encode(password), Role.valueOf(role));
        return adminRepository.save(admin);
    }

    public void deleteAdmin(Admin admin) {
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

    @Transactional
    public void updateAdminRole(Long adminId, Role newRole, Long loggedInAdminId) {

        // 자기 자신의 Role 변경 방지
        if (loggedInAdminId.equals(adminId)) {
            throw new CannotChangeOwnRoleException();
        }
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(AdminNotFoundException::new);

        admin.updateRole(newRole);
        adminRepository.save(admin);
    }

}