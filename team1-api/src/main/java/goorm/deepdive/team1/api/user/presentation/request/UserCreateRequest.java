package goorm.deepdive.team1.api.user.presentation.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
	@Schema(description = "이름", example = "박민준", requiredMode = REQUIRED)
	@NotBlank
	String name,

	@Schema(description = "이메일", example = "alswns11346@kgu.ac.kr", requiredMode = REQUIRED)
	@NotBlank
	String email,

	@Schema(description = "휴대폰 번호", example = "01012345678", requiredMode = REQUIRED)
	@NotBlank
	String phoneNumber
) {
}
