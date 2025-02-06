package goorm.deepdive.team1.api.admin.presentation.response;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import lombok.Getter;

public record AdminRegisterResponse(
        Long id,
        String email,
        String token
) {
    public static AdminRegisterResponse from(Long id, String email, String token) {
        return new AdminRegisterResponse(id, email, token);
    }
}