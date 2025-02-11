package goorm.deepdive.team1.domain.user.application;

import goorm.deepdive.team1.domain.user.exception.UserEmailAlreadyExistsException;
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

	public User create(String name, String email, String phoneNumber, String gender, Integer age) {
		User user = User.create(name, email, phoneNumber, gender, age);
		return userRepository.save(user);
	}

	public void update(Long id, String name, String email, String phoneNumber, String gender, Integer age) {

		User user = getUser(id);

		if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
			throw new UserEmailAlreadyExistsException();
		}

		user.updateName(name);
		user.updateEmail(email);
		user.updatePhoneNumber(phoneNumber);
		user.updateGender(gender);
		user.updateAge(age);
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
