package goorm.deepdive.team1.domain.user.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.exception.UserNotFoundException;
import goorm.deepdive.team1.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {
	private final UserRepository userRepository;

	public UserCache getUserCacheById(Long id) {
		return userRepository.getUserCache(id);
	}

	public User getById(Long id) {
		return userRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(UserNotFoundException::new);
	}

	public Page<UserCache> getAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	public Page<User> getUsersByRoadAddressKeyword(String keyword, Pageable pageable) {
		return userRepository.findUsersByRoadAddressKeyword(keyword, pageable);
	}

	public Page<User> getUsersByRegionAddressKeyword(String keyword, Pageable pageable) {
		return userRepository.findUsersByRegionAddressKeyword(keyword, pageable);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
}
