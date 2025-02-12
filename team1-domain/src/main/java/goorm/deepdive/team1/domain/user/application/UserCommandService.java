package goorm.deepdive.team1.domain.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.domain.user.domain.enums.Gender;
import goorm.deepdive.team1.domain.user.exception.UserEmailAlreadyExistsException;
import goorm.deepdive.team1.domain.user.exception.UserNotFoundException;
import goorm.deepdive.team1.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {
	private final UserRepository userRepository;

	public User create(String name, String email, String phoneNumber, Address address, Gender gender, Integer age) {
		User user = User.create(name, email, phoneNumber, address, gender, age);
		return userRepository.save(user);
	}

	public void update(Long id, String name, String email, String phoneNumber, Gender gender, Integer age) {

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

	public void saveCache(UserCache userCache) {
		userRepository.saveCache(userCache);
	}

	public void saveDocument(UserDocument userDocument) {
		userRepository.saveDocument(userDocument);
	}
}
