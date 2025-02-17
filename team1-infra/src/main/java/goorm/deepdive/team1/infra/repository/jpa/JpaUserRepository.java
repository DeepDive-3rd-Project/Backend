package goorm.deepdive.team1.infra.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import goorm.deepdive.team1.domain.user.domain.User;

public interface JpaUserRepository extends JpaRepository<User, Long> {
	Page<User> findAllByDeletedAtIsNull(Pageable pageable);

	Optional<User> findByIdAndDeletedAtIsNull(Long id);

	boolean existsByEmail(String email);

	@Query("SELECT u.id FROM User u WHERE u.deletedAt IS NOT NULL")
	List<Long> findIdsByDeletedAtIsNotNull();

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	void deleteAllByDeletedAtIsNotNull();
}
