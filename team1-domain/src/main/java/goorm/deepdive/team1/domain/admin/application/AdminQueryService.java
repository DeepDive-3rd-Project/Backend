package goorm.deepdive.team1.domain.admin.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.exception.AdminNotFoundException;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQueryService {
    private final AdminRepository adminRepository;

    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    public Page<Admin> getAdminsByPage(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(AdminNotFoundException::new);
    }

    public Admin getById(Long id) {
        return adminRepository.findById(id)
            .orElseThrow(AdminNotFoundException::new);
    }
}
