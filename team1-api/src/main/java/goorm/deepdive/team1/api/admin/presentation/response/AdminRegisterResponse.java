package goorm.deepdive.team1.api.admin.presentation.response;

import goorm.deepdive.team1.domain.admin.domain.Admin;
import lombok.Getter;

public record AdminRegisterResponse(
        Long id,
        String email,
        String token
) {
    public static AdminRegisterResponse from(Admin admin, String token) {
        return new AdminRegisterResponse(admin.getId(), admin.getEmail(), token);
    }
}