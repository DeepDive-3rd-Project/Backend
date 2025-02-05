package goorm.deepdive.team1.api.address.presentation.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressUpdateRequest(
	@Schema(description = "경도", example = "127.024612", requiredMode = REQUIRED)
	@NotNull
	double x,

	@Schema(description = "위도", example = "37.532600", requiredMode = REQUIRED)
	@NotNull
	double y,

	@Schema(description = "지번 주소", example = "서울특별시 강남구 역삼동 648-23 여삼빌딩", requiredMode = REQUIRED)
	@NotBlank
	String regionAddress,

	@Schema(description = "도로명 주소", example = "서울특별시 강남구 테헤란로 123", requiredMode = REQUIRED)
	@NotBlank
	String roadAddress
) {
}
