package goorm.deepdive.team1.common.exception.KakaoApiException;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.common.exception.KakaoApiException.KakaoApiExceptionCode.INVALID_REQUEST;

public class InvalidRequestException extends CustomException {
    public InvalidRequestException() {super(INVALID_REQUEST);}
}
