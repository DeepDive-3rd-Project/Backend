package goorm.deepdive.team1.domain.admin.exception;

import goorm.deepdive.team1.common.exception.CustomException;

import static goorm.deepdive.team1.domain.admin.exception.AdminDomainExceptionCode.PASSWORD_MISMATCH;

public class PasswordMismatchException extends CustomException {
    public PasswordMismatchException() {
        super(PASSWORD_MISMATCH);
    }
}
