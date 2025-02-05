package goorm.deepdive.team1.common.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminExceptionCode implements ExceptionCode {
	NOT_ADMIN(FORBIDDEN, "관리자 전용 API입니다."),
	;

	private final HttpStatus status;
	private final String message;


	@Override
	public String getCode() {
		return this.name();
	}
}
