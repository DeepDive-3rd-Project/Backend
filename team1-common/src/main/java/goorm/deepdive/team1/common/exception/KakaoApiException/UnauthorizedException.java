package goorm.deepdive.team1.common.exception.KakaoApiException;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.common.exception.KakaoApiException.KakaoApiExceptionCode.UNAUTHORIZED;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException() {
        super(UNAUTHORIZED);
    }
}
