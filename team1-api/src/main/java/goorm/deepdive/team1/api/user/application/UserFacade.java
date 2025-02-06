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
import goorm.deepdive.team1.domain.address.application.AddressQueryService;
import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryCommandService;
import goorm.deepdive.team1.domain.user.application.UserCommandService;
import goorm.deepdive.team1.domain.user.application.UserQueryService;
import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;
	private final AddressHistoryCommandService addressHistoryCommandService;
	private final AddressCommandService addressCommandService;
	private final AddressQueryService addressQueryService;

	@Transactional
	public UserPersistResponse create(UserCreateRequest request) {
		User user = userCommandService.create(request.name(), request.email(), request.phoneNumber());
		Address address = null;
		try {
			address = addressQueryService.getByAddress(request.address());
		} catch (UserNotFoundException e) {
			// todo : 카카오 api를 이용하여 주소 데이터를 가져와서 저장
			// address = addressCommandService.create();
			// addressCommandService.save(address);
		}
		addressHistoryCommandService.create(user, address);

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
