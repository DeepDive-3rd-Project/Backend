package goorm.deepdive.team1.api.user.application;

import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.FIFTIES;
import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.FORTIES;
import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.SIXTIES_AND_ABOVE;
import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.TEENS;
import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.THIRTIES;
import static goorm.deepdive.team1.domain.user.domain.enums.AgeGroups.TWENTIES;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	public UserCache getUserCacheById(Long id) {
		UserCache userCache = userQueryService.getUserCacheById(id);

		if (userCache == null) {
			User user = userQueryService.getById(id);
			userCache = UserCache.from(user);
			userCommandService.saveCache(userCache);
		}

		return userCache;
	}

	public PaginatedListResponse getAll(Pageable pageable) {
		Page<UserCache> userCaches = userQueryService.getCaches(pageable);

		if (userCaches.hasContent() && userCaches.getContent().size() == pageable.getPageSize()) {
			return PaginatedListResponse.from(userCaches);
		}

		userCaches = userQueryService.getCachesFromUsers(pageable);
		userCommandService.saveAllCaches(userCaches.getContent());
		return PaginatedListResponse.from(userCaches);
	}

	@Transactional
	public void update(Long id, UserUpdateRequest request) {
		Address address = addressCommandService.findOrCreateAddress(
			request.regionAddress(), request.roadAddress()
		);
		User user = userQueryService.getById(id);

		boolean isAddressChanged = !Objects.equals(address.getId(), user.getAddress().getId());

		userCommandService.update(user, request.name(), request.email(), request.phoneNumber(),
			request.gender(), request.age(), address);

		if (isAddressChanged) {
			AddressHistory addressHistory = addressHistoryCommandService.create(user, address);
			addressHistoryProducer.sendMessageToDelete(addressHistory);
		}

		userProducer.sendMessageToUpdate(user);
	}

	@Transactional
	public void delete(Long id) {
		User user = userQueryService.getById(id);
		userCommandService.delete(user);
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

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void cleanUpDeletedUsers() {
		log.info("🗑️ 유저 삭제 스케줄링 작동...");
		List<Long> userIdsToDelete = userQueryService.findIdsByDeletedAtIsNotNull();

		if (userIdsToDelete.isEmpty()) {
			log.info("✅ 삭제할 유저 데이터가 없습니다.");
			return;
		}
		addressHistoryCommandService.cleanUpDeletedAddressHistories(userIdsToDelete);
		userCommandService.cleanUpDeletedUsers(userIdsToDelete);
	}
}
