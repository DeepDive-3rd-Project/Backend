package goorm.deepdive.team1.domain.admin.application;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
