package goorm.deepdive.team1.api.admin.presentation.request;

public record AdminRegisterRequest(
        String email,
        String password,
        String role
) {
}