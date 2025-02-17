package goorm.deepdive.team1.api.admin.presentation.response;

import goorm.deepdive.team1.domain.admin.domain.Admin;

public record AdminListResponse(
        Long id,
        String email,
        String role
) {
    public static AdminListResponse fromEntity(Admin admin) {
        return new AdminListResponse(admin.getId(), admin.getEmail(), admin.getRole().name());
    }
}