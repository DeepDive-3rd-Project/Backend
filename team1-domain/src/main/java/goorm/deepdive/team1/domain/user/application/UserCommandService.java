package goorm.deepdive.team1.domain.user.application;

import java.util.List;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandService {
	private final UserRepository userRepository;

	public User create(String name, String email, String phoneNumber, Address address, Gender gender, Integer age) {
		User user = User.create(name, email, phoneNumber, address, gender, age);
		return userRepository.save(user);
	}

	@Transactional
	public User update(Long id, String name, String email, String phoneNumber, Gender gender, Integer age, Address address) {
		User user = getUser(id);

		if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
			throw new UserEmailAlreadyExistsException();
		}

		user.updateName(name);
		user.updateEmail(email);
		user.updatePhoneNumber(phoneNumber);
		user.updateGender(gender);
		user.updateAge(age);
		user.updateAddress(address);

		return user;
	}

	public boolean validateEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Transactional
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

	public void cleanUpDeletedUsers(List<Long> ids) {
		userRepository.deleteScheduling(ids);
		log.info("✅ Deleted {} users successfully!", ids.size());
	}
}
