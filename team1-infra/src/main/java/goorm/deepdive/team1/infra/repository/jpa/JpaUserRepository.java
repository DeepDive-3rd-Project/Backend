package goorm.deepdive.team1.infra.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import goorm.deepdive.team1.domain.user.domain.User;

public interface JpaUserRepository extends JpaRepository<User, Long> {
	Page<User> findAllByDeletedAtIsNull(Pageable pageable);

	Optional<User> findByIdAndDeletedAtIsNull(Long id);

	boolean existsByEmail(String email);

	List<Long> findIdsByDeletedAtIsNotNull();

	void deleteAllByDeletedAtIsNotNull();
}
