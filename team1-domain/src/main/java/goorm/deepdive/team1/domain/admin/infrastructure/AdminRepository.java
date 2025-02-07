package goorm.deepdive.team1.domain.admin.infrastructure;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import java.util.Optional;

public interface AdminRepository {
    Admin save(Admin admin);
    Optional<Admin> findByEmail(String email);
}
