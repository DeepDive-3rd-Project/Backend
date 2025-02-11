package goorm.deepdive.team1.common.exception.KakaoApiException;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.common.exception.KakaoApiException.KakaoApiExceptionCode.RATE_LIMIT_EXCEEDED;

public class RateLimitExceededException extends CustomException {
    public RateLimitExceededException() {
        super(RATE_LIMIT_EXCEEDED);
    }
}
