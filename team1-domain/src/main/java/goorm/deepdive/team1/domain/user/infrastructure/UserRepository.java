package goorm.deepdive.team1.domain.user.infrastructure;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;

public interface UserRepository {
	User save(User user);

	Optional<User> findByIdAndDeletedAtIsNull(Long id);

	Optional<UserCache> getUserCache(Long id);

	Page<UserCache> findAll(Pageable pageable);

	void deleteById(Long id);

	boolean existsById(Long id);

	Page<User> findUsersByRoadAddressKeyword(String keyword, Pageable pageable);

	Page<User> findUsersByRegionAddressKeyword(String keyword, Pageable pageable);
}
