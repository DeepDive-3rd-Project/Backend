package goorm.deepdive.team1.domain.user.application;

import goorm.deepdive.team1.common.exception.CustomException;
import goorm.deepdive.team1.domain.user.exception.UserDomainExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.exception.UserNotFoundException;
import goorm.deepdive.team1.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {
	private final UserRepository userRepository;

	public User create(String name, String email, String phoneNumber) {
		User user = User.create(name, email, phoneNumber);
		return userRepository.save(user);
	}

	public void update(Long id, String name, String email, String phoneNumber) {

		User user = getUser(id);

		if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
			throw new CustomException(UserDomainExceptionCode.EMAIL_ALREADY_EXISTS);
		}

		user.updateName(name);
		user.updateEmail(email);
		user.updatePhoneNumber(phoneNumber);
	}

	public void delete(Long id) {
		User user = getUser(id);
		user.markAsDeleted();
	}

	private User getUser(Long id) {
		return userRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(UserNotFoundException::new);
	}
}
