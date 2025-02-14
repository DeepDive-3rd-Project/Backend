package goorm.deepdive.team1.api.user.application;

import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.FIFTIES;
import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.FORTIES;
import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.SIXTIES_AND_ABOVE;
import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.TEENS;
import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.THIRTIES;
import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.TWENTIES;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.api.paging.PaginatedListResponse;
import goorm.deepdive.team1.api.user.presentation.request.UserCreateRequest;
import goorm.deepdive.team1.api.user.presentation.request.UserUpdateRequest;
import goorm.deepdive.team1.api.user.presentation.resonse.UserPersistResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserStatsResponse;
import goorm.deepdive.team1.domain.address.application.AddressCommandService;
import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryCommandService;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.user.application.UserCommandService;
import goorm.deepdive.team1.domain.user.application.UserQueryService;
import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.domain.user.domain.enums.AgeGroups;
import goorm.deepdive.team1.infra.kafka.producer.AddressHistoryProducer;
import goorm.deepdive.team1.infra.kafka.producer.UserProducer;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;
	private final AddressHistoryCommandService addressHistoryCommandService;
	private final AddressCommandService addressCommandService;
	private final UserProducer userProducer;
	private final AddressHistoryProducer addressHistoryProducer;

	@Transactional
	public UserPersistResponse create(UserCreateRequest request) {
		Address address = addressCommandService.findOrCreateAddress(
				request.regionAddress(), request.roadAddress()
		);

		User user = userCommandService.create(
			request.name(),
			request.email(),
			request.phoneNumber(),
			address,
			request.gender(),
			request.age()
		);

		AddressHistory addressHistory = addressHistoryCommandService.create(user, address);

		userProducer.sendMessageToCreate(user);
		addressHistoryProducer.sendMessageToCreate(addressHistory);

		return UserPersistResponse.from(user);
	}

	@Transactional
	public UserCache getUserCacheById(Long id) {
		UserCache userCache;
		userCache = userQueryService.getUserCacheById(id);

		if (userCache == null) {
			User user = userQueryService.getById(id);
			userCache = UserCache.from(user);
			userCommandService.saveCache(userCache);
		}

		return userCache;
	}

	public PaginatedListResponse getAll(Pageable pageable) {
		Page<UserCache> userList = userQueryService.getAll(pageable);
		return PaginatedListResponse.from(userList);
	}

	@Transactional
	public void update(Long id, UserUpdateRequest request) {
		Address address = addressCommandService.findOrCreateAddress(
			request.regionAddress(), request.roadAddress()
		);

		User user = userCommandService.update(id, request.name(), request.email(), request.phoneNumber(), request.gender(), request.age(), address);
		AddressHistory addressHistory = addressHistoryCommandService.create(user, address);

		userProducer.sendMessageToUpdate(user);
		addressHistoryProducer.sendMessageToDelete(addressHistory);
	}

	public void delete(Long id) {
		userCommandService.delete(id);
	}

	public PaginatedListResponse searchUsersByRoadAddressKeyword(String keyword, Pageable pageable) {
		Page<UserDocument> userList = userQueryService.getUsersByRoadAddressKeyword(keyword, pageable);
		return PaginatedListResponse.from(userList);
	}

	public PaginatedListResponse searchUsersByRegionAddressKeyword(String keyword, Pageable pageable) {
		Page<UserDocument> userList = userQueryService.getUsersByRegionAddressKeyword(keyword, pageable);
		return PaginatedListResponse.from(userList);
	}

	public PaginatedListResponse searchUsersByName(String name, Pageable pageable) {
		Page<UserDocument> userList = userQueryService.getUsersByName(name, pageable);
		return PaginatedListResponse.from(userList);
	}

	public UserStatsResponse getUserStatistics(List<String> gender, List<String> region, List<AgeGroups> ageGroups) {
		List<AgeGroups> ageGroup = (ageGroups == null || ageGroups.isEmpty())
			? List.of(TEENS, TWENTIES, THIRTIES, FORTIES, FIFTIES, SIXTIES_AND_ABOVE) : ageGroups;

		Map<String, Object> stats = userQueryService.getUserStatistics(gender, region, ageGroup);
		return UserStatsResponse.from(stats);
	}

	public List<UserDocument> getUserHeatMap(List<String> region, List<AgeGroups> ageGroups) {
		List<AgeGroups> ageGroup = (ageGroups == null || ageGroups.isEmpty())
			? List.of(TEENS, TWENTIES, THIRTIES, FORTIES, FIFTIES, SIXTIES_AND_ABOVE) : ageGroups;

		return userQueryService.getUsersHeatMap(region, ageGroup);
	}
}
