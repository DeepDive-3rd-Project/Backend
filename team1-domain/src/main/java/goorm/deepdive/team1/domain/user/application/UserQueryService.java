package goorm.deepdive.team1.domain.user.application;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.domain.user.domain.enums.AgeGroups;
import goorm.deepdive.team1.domain.user.exception.UserNotFoundException;
import goorm.deepdive.team1.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {
	private final UserRepository userRepository;

	public UserCache getUserCacheById(Long id) {
		UserCache userCache = userRepository.getUserCache(id);
		if (userCache == null) {
			User user = getById(id);
			userCache = UserCache.from(user);
			userRepository.saveCache(userCache);
		}
		return userCache;
	}

	private User getById(Long id) {
		return userRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(UserNotFoundException::new);
	}

	public Page<UserCache> getAll(Pageable pageable) {
		Page<UserCache> userCaches = userRepository.findAllCache(pageable);
		if (userCaches.hasContent() && userCaches.getContent().size() == pageable.getPageSize()) {
			return userCaches;
		}

		Page<User> users = userRepository.findAll(pageable);
		if (users.isEmpty()) {
			return Page.empty();
		}

		List<UserCache> cacheUsers = users.getContent().stream()
			.map(UserCache::from)
			.toList();

		userRepository.saveAllCache(cacheUsers);

		return new PageImpl<>(cacheUsers, pageable, users.getTotalElements());
	}

	public Page<UserDocument> getUsersByRoadAddressKeyword(String keyword, Pageable pageable) {
		return userRepository.searchByRoadAddress(keyword, pageable);
	}

	public Page<UserDocument> getUsersByRegionAddressKeyword(String keyword, Pageable pageable) {
		return userRepository.searchByRegionAddress(keyword, pageable);
	}

	public Page<UserDocument> getUsersByName(String name, Pageable pageable) {
		return userRepository.searchByName(name, pageable);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public Map<String, Object> getUserStatistics(List<String> gender, List<String> region, List<AgeGroups> ageGroups) {
		return userRepository.searchUserStatistics(gender, region, ageGroups);
	}

	public List<UserDocument> getUsersHeatMap(List<String> region, List<AgeGroups> ageGroups) {
		return userRepository.searchHeatMap(region, ageGroups);
	}

	public List<Long> findIdsByDeletedAtIsNotNull() {
		return userRepository.findIdsByDeletedAtIsNotNull();
	}
}
