package goorm.deepdive.team1.domain.admin.security;

public interface JwtTokenProvider {
    String generateToken(String email, String role);
}