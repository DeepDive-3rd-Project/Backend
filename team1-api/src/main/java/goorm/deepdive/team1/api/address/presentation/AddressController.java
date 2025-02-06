package goorm.deepdive.team1.api.address.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import goorm.deepdive.team1.api.address.presentation.request.AddressCreateRequest;
import goorm.deepdive.team1.api.address.presentation.request.AddressUpdateRequest;
import goorm.deepdive.team1.api.address.presentation.resonse.AddressListResponse;
import goorm.deepdive.team1.api.address.presentation.resonse.AddressPersistResponse;
import goorm.deepdive.team1.api.address.presentation.resonse.AddressResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "ADDRESS", description = "주소 API")
public interface AddressController {

	@Operation(summary = "주소 추가 API", description = """
			- Description : 이 API는 주소 데이터를 추가할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "201",
		content = @Content(schema = @Schema(implementation = AddressPersistResponse.class))
	)
	ResponseEntity<AddressPersistResponse> create(
		@Valid @RequestBody AddressCreateRequest request
	);

	@Operation(summary = "주소 상세 조회 API", description = """
			- Description : 이 API는 주소 데이터를 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = AddressResponse.class))
	)
	ResponseEntity<AddressResponse> getById(
		@Parameter(description = "조회할 주소의 ID", example = "1")
		@PathVariable Long id
	);

	@Operation(summary = "주소 목록 조회 API", description = """
			- Description : 이 API는 주소 데이터 목록을 조회할 수 있습니다.
		""")
	@ApiResponse(
		responseCode = "200",
		content = @Content(schema = @Schema(implementation = AddressListResponse.class))
	)
	ResponseEntity<AddressListResponse> getAllByDeletedAtIsNull();

	@Operation(summary = "주소 수정 API", description = """
			- Description : 이 API는 주소 데이터를 수정할 수 있습니다.
		""")
	@ApiResponse(responseCode = "204")
	ResponseEntity<Void> update(
		@Parameter(description = "수정할 주소의 ID", example = "1")
		@PathVariable Long id,

		@Valid
		@RequestBody
		AddressUpdateRequest request
	);

	@Operation(summary = "주소 삭제 API", description = """
			- Description : 이 API는 주소 데이터를 삭제할 수 있습니다.
		""")
	@ApiResponse(responseCode = "204")
	ResponseEntity<Void> delete(
		@Parameter(description = "삭제할 주소의 ID", example = "1")
		@PathVariable Long id
	);
}
