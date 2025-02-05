package goorm.deepdive.team1.domain.user.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.exception.UserNotFoundException;
import goorm.deepdive.team1.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {
	private final UserRepository userRepository;

	public User getById(Long id) {
		return userRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(UserNotFoundException::new);
	}

	public List<User> getAllByDeletedAtIsNull() {
		return userRepository.findAllByDeletedAtIsNull();
	}
}
