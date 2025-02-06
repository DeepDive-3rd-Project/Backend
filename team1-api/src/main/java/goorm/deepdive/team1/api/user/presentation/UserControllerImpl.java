package goorm.deepdive.team1.api.user.presentation;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.deepdive.team1.api.user.application.UserFacade;
import goorm.deepdive.team1.api.user.presentation.request.UserCreateRequest;
import goorm.deepdive.team1.api.user.presentation.request.UserUpdateRequest;
import goorm.deepdive.team1.api.user.presentation.resonse.UserListResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserPersistResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController{
	private final UserFacade userFacade;

	@Override
	@PostMapping
	public ResponseEntity<UserPersistResponse> create(UserCreateRequest request) {
		UserPersistResponse response = userFacade.create(request);
		return ResponseEntity.status(CREATED).body(response);
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getById(Long id) {
		UserResponse response = userFacade.getById(id);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping
	public ResponseEntity<UserListResponse> getAllByDeletedAtIsNull() {
		UserListResponse response = userFacade.getAllByDeletedAtIsNull();
		return ResponseEntity.ok(response);
	}

	@Override
	@PatchMapping("/{id}")
	public ResponseEntity<Void> update(Long id, UserUpdateRequest request) {
		userFacade.update(id, request);
		return ResponseEntity.noContent().build();
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(Long id) {
		userFacade.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@GetMapping("/search")
	public ResponseEntity<UserListResponse> searchUsersByAddressKeyword(String keyword) {
		UserListResponse response = userFacade.searchUsersByAddressKeyword(keyword);
		return ResponseEntity.ok(response);
	}
}
