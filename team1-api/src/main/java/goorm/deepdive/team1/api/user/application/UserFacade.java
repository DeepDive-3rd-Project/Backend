package goorm.deepdive.team1.api.user.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import goorm.deepdive.team1.api.kakao.response.KakaoApiAddressResponse;
import goorm.deepdive.team1.api.kakao.KakaoApiAddressService;
import goorm.deepdive.team1.domain.user.exception.UserEmailAlreadyExistsException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import goorm.deepdive.team1.domain.address.application.AddressCommandService;
import goorm.deepdive.team1.domain.address.application.AddressQueryService;
import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryCommandService;
import goorm.deepdive.team1.domain.user.application.UserCommandService;
import goorm.deepdive.team1.domain.user.application.UserQueryService;
import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.api.paging.PaginatedListResponse;
import goorm.deepdive.team1.api.user.presentation.request.UserCreateRequest;
import goorm.deepdive.team1.api.user.presentation.request.UserUpdateRequest;
import goorm.deepdive.team1.api.user.presentation.resonse.UserPersistResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;
	private final AddressHistoryCommandService addressHistoryCommandService;
	private final AddressCommandService addressCommandService;
	private final AddressQueryService addressQueryService;
	private final KakaoApiAddressService kakaoApiAddressService;

	@Transactional
	public UserPersistResponse create(UserCreateRequest request) {

		if (userQueryService.existsByEmail(request.email())) {
			throw new UserEmailAlreadyExistsException();
		}

		User user = userCommandService.create(request.name(), request.email(), request.phoneNumber(), request.gender(), request.age());

		KakaoApiAddressResponse kakaoApiAddressResponse = kakaoApiAddressService.getGeoDataFromAddress(request.address());
		Address address = addressQueryService.findByRegionOrRoadAddress(
				kakaoApiAddressResponse.regionAddress(),
				kakaoApiAddressResponse.roadAddress()
		);

		if (address == null) {
			address = addressCommandService.create(
					kakaoApiAddressResponse.x(),
					kakaoApiAddressResponse.y(),
					kakaoApiAddressResponse.regionAddress(),
					kakaoApiAddressResponse.roadAddress(),
					kakaoApiAddressResponse.region()
			);
			addressCommandService.save(address);
		}

		addressHistoryCommandService.create(user, address);

		return UserPersistResponse.from(user);
	}

	public UserCache getUserCacheById(Long id) {
		return userQueryService.getUserCacheById(id);
	}

	public PaginatedListResponse getAll(Pageable pageable) {
		Page<UserCache> userList = userQueryService.getAll(pageable);
		return PaginatedListResponse.from(userList);
	}

	public void update(Long id, UserUpdateRequest request) {
		userCommandService.update(id, request.name(), request.email(), request.phoneNumber(), request.gender(), request.age());
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
}
