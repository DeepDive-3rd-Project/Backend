package goorm.deepdive.team1.domain.user.application;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.address.domain.AddressSearch;
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
		return userRepository.getUserCache(id);
	}

	public User getById(Long id) {
		return userRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(UserNotFoundException::new);
	}

	public Page<UserCache> getAll(Pageable pageable) {
		return userRepository.findAll(pageable);
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
}
