package goorm.deepdive.team1.api.user.application;

import java.util.List;

import org.springframework.stereotype.Component;

import goorm.deepdive.team1.api.user.presentation.request.UserCreateRequest;
import goorm.deepdive.team1.api.user.presentation.request.UserUpdateRequest;
import goorm.deepdive.team1.api.user.presentation.resonse.UserListResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserPersistResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserResponse;
import goorm.deepdive.team1.domain.user.application.UserCommandService;
import goorm.deepdive.team1.domain.user.application.UserQueryService;
import goorm.deepdive.team1.domain.user.domain.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;

	public UserPersistResponse create(UserCreateRequest request) {
		User user = userCommandService.create(request.name(), request.email(), request.phoneNumber());
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
}
