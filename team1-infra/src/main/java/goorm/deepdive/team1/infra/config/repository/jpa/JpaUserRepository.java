package goorm.deepdive.team1.infra.config.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import goorm.deepdive.team1.domain.user.domain.User;

public interface JpaUserRepository extends JpaRepository<User, Long> {
	List<User> findAllByDeletedAtIsNull();

	Optional<User> findByIdAndDeletedAtIsNull(Long id);
}
