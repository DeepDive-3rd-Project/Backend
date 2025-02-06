package goorm.deepdive.team1.infra.config.repository.impl;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.infrastructure.AdminRepository;
import goorm.deepdive.team1.infra.config.repository.jpa.JpaAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {
    private final JpaAdminRepository jpaAdminRepository;

    @Override
    public Admin save(Admin admin) {
        return jpaAdminRepository.save(admin);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return jpaAdminRepository.findByEmail(email);
    }
}