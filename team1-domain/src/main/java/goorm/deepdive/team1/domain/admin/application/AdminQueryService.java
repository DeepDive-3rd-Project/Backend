package goorm.deepdive.team1.domain.admin.application;

import goorm.deepdive.team1.common.exception.AdminExceptionCode;
import goorm.deepdive.team1.common.exception.CustomException;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQueryService {
    private final AdminRepository adminRepository;

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(AdminExceptionCode.ADMIN_NOT_FOUND));
    }

    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

}
