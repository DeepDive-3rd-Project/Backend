package goorm.deepdive.team1.api.user.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.api.user.presentation.request.UserCreateRequest;
import goorm.deepdive.team1.api.user.presentation.request.UserUpdateRequest;
import goorm.deepdive.team1.api.user.presentation.resonse.UserListResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserPersistResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserResponse;
import goorm.deepdive.team1.domain.address.application.AddressCommandService;
import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryCommandService;
import goorm.deepdive.team1.domain.user.application.UserCommandService;
import goorm.deepdive.team1.domain.user.application.UserQueryService;
import goorm.deepdive.team1.domain.user.domain.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;
	private final AddressHistoryCommandService addressHistoryCommandService;
	private final AddressCommandService addressCommandService;

	@Transactional
	public UserPersistResponse create(UserCreateRequest request) {
		User user = userCommandService.create(request.name(), request.email(), request.phoneNumber());
		/* todo : 유저 생성 시 주소 같이 저장 로직 구현
		Address address = addressSearchService(request.address());
		addressCommandService.create(address);

		addressHistoryCommandService.create(user, address);
		*/
		return UserPersistResponse.from(user);
	}

	public UserResponse getById(Long id) {
		User user = userQueryService.getById(id);
		return UserResponse.from(user);
	}

	public UserListResponse getAllByDeletedAtIsNull() {
		List<User> userList = userQueryService.getAllByDeletedAtIsNull();
		return UserListResponse.from(userList);
	}

	public void update(Long id, UserUpdateRequest request) {
		userCommandService.update(id, request.name(), request.email(), request.phoneNumber());
	}

	public void delete(Long id) {
		userCommandService.delete(id);
	}

	public UserListResponse searchUsersByAddressKeyword(String keyword) {
		List<User> userList = userQueryService.getUsersByAddressKeyword(keyword);
		return UserListResponse.from(userList);
	}
}
