package goorm.deepdive.team1.api.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import goorm.deepdive.team1.api.user.presentation.request.UserCreateRequest;
import goorm.deepdive.team1.api.user.presentation.request.UserUpdateRequest;
import goorm.deepdive.team1.api.user.presentation.resonse.UserListResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserPersistResponse;
import goorm.deepdive.team1.api.user.presentation.resonse.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "USER", description = "유저 API")
public interface UserController {

	@Operation(summary = "유저 추가 API", description = """
			- Description : 이 API는 유저 데이터를 추가할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "201",
		content = @Content(schema = @Schema(implementation = UserPersistResponse.class))
	)
	ResponseEntity<UserPersistResponse> create(
		@Valid @RequestBody UserCreateRequest request
	);

	@Operation(summary = "유저 상세 조회 API", description = """
			- Description : 이 API는 유저 데이터를 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = UserResponse.class))
	)
	ResponseEntity<UserResponse> getById(
		@Parameter(description = "조회할 사용자의 ID", example = "1")
		@PathVariable Long id
	);

	@Operation(summary = "유저 목록 조회 API", description = """
			- Description : 이 API는 유저 데이터 목록을 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = UserListResponse.class))
	)
	ResponseEntity<UserListResponse> getAllByDeletedAtIsNull();

	@Operation(summary = "유저 수정 API", description = """
			- Description : 이 API는 유저 데이터를 수정할 수 있습니다.
		""")
	@ApiResponse(responseCode = "204")
	ResponseEntity<Void> update(
		@Parameter(description = "수정할 사용자의 ID", example = "1")
		@PathVariable Long id,

		@Valid
		@RequestBody
		UserUpdateRequest request
	);

	@Operation(summary = "유저 삭제 API", description = """
			- Description : 이 API는 유저 데이터를 삭제할 수 있습니다.
		""")
	@ApiResponse(responseCode = "204")
	@PatchMapping("/{id}")
	ResponseEntity<Void> delete(
		@Parameter(description = "삭제할 사용자의 ID", example = "1")
		@PathVariable Long id
	);
}
