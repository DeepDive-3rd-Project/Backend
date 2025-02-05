package goorm.deepdive.team1.api.address.presentation.resonse;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import goorm.deepdive.team1.domain.address.domain.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddressResponse(
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
	public static AddressResponse from(Address address) {
		return AddressResponse.builder()
			.x(address.getX())
			.y(address.getY())
			.regionAddress(address.getRegionAddress())
			.roadAddress(address.getRoadAddress())
			.build();
	}
}
