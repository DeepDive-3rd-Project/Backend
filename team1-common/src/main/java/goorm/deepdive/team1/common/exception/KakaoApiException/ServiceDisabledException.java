package goorm.deepdive.team1.common.exception.KakaoApiException;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.common.exception.KakaoApiException.KakaoApiExceptionCode.SERVICE_DISABLED;

public class ServiceDisabledException extends CustomException {
    public ServiceDisabledException() {
        super(SERVICE_DISABLED);
    }
}
