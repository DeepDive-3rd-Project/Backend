package goorm.deepdive.team1.domain.user.infrastructure;

import java.util.List;
import java.util.Optional;

import goorm.deepdive.team1.domain.user.domain.User;

public interface UserRepository {
	User save(User user);

	Optional<User> findByIdAndDeletedAtIsNull(Long id);

	List<User> findAllByDeletedAtIsNull();

	void deleteById(Long id);

	boolean existsById(Long id);

	List<User> findUsersByAddressKeyword(String keyword);
}
