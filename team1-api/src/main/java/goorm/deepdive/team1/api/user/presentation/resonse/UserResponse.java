package goorm.deepdive.team1.api.user.presentation.resonse;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import goorm.deepdive.team1.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserResponse(
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
	public static UserResponse from(User user) {
		return UserResponse.builder()
			.name(user.getName())
			.email(user.getEmail())
			.phoneNumber(user.getPhoneNumber())
			.build();
	}
}
