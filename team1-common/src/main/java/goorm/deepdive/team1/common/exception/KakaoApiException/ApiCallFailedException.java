package goorm.deepdive.team1.common.exception.KakaoApiException;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.common.exception.KakaoApiException.KakaoApiExceptionCode.API_CALL_FAILED;

public class ApiCallFailedException extends CustomException {
    public ApiCallFailedException() {
        super(API_CALL_FAILED);
    }
}
