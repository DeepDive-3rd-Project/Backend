package goorm.deepdive.team1.domain.admin.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Page<Admin> getAdminsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
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
