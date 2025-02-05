package goorm.deepdive.team1.domain.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.user.domain.User;
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

	public void update(User user, String name, String email, String phoneNumber) {
		user.updateName(name);
		user.updateEmail(email);
		user.updatePhoneNumber(phoneNumber);
	}

	public void delete(User user) {
		user.markAsDeleted();
	}
}
