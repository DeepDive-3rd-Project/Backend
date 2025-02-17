package goorm.deepdive.team1.domain.user.application;

import java.util.List;

import org.springframework.stereotype.Service;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.enums.Gender;
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

	public void update(User user, String name, String email, String phoneNumber, Gender gender, Integer age, Address address) {
		user.updateName(name);
		user.updateEmail(email);
		user.updatePhoneNumber(phoneNumber);
		user.updateGender(gender);
		user.updateAge(age);
		user.updateAddress(address);
	}

	public void delete(User user) {
		user.markAsDeleted();
	}

	public void saveCache(UserCache userCache) {
		userRepository.saveCache(userCache);
	}

	public void cleanUpDeletedUsers(List<Long> ids) {
		userRepository.deleteScheduling(ids);
		log.info("✅ {}명의 유저 데이터가 삭제 되었습니다", ids.size());
	}

	public void saveAllCaches(List<UserCache> userCaches) {
		userRepository.saveAllCache(userCaches);
	}
}
