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

    public Admin register(String email, String password, Role role) {
        Admin admin = Admin.create(email, passwordEncryptor.encode(password), role);
        return adminRepository.save(admin);
    }

    public void deleteAdmin(Admin admin) {
        adminRepository.delete(admin);
    }

    @Transactional
    public void updatePassword(Admin admin, String oldPassword, String newPassword) {
        if (!passwordEncryptor.matches(oldPassword, admin.getPassword())) {
            throw new AdminPasswordMismatchException();
        }

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