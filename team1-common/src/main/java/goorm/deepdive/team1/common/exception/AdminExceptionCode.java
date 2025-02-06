package goorm.deepdive.team1.common.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminExceptionCode implements ExceptionCode {
	NOT_ADMIN(FORBIDDEN, "관리자 전용 API입니다."),
	ADMIN_NOT_FOUND(NOT_FOUND, "해당 이메일의 관리자를 찾을 수 없습니다.");
	;

	private final HttpStatus status;
	private final String message;


	@Override
	public String getCode() {
		return this.name();
	}
}
