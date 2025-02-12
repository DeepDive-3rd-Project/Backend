package goorm.deepdive.team1.api.admin.application;

import goorm.deepdive.team1.api.admin.presentation.request.AdminRegisterRequest;
import goorm.deepdive.team1.api.admin.presentation.response.AdminRegisterResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminReissueResponse;
import goorm.deepdive.team1.api.jwt.CookieUtil;
import goorm.deepdive.team1.api.jwt.JwtUtil;
import goorm.deepdive.team1.common.exception.AdminExceptionCode;
import goorm.deepdive.team1.common.exception.CustomException;
import goorm.deepdive.team1.common.exception.JwtExceptionCode;
import goorm.deepdive.team1.domain.admin.application.AdminCommandService;
import goorm.deepdive.team1.domain.admin.application.AdminQueryService;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.infrastructure.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminFacade {
    private final AdminCommandService adminCommandService;
    private final AdminQueryService adminQueryService;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public AdminRegisterResponse register(AdminRegisterRequest request) {
        if (adminQueryService.existsByEmail(request.email())) {
            throw new CustomException(AdminExceptionCode.ALREADY_REGISTERED);
        }

        Admin admin = adminCommandService.register(request.email(), request.password(), request.role());
        return new AdminRegisterResponse(admin.getId(), admin.getEmail());
    }



    public AdminReissueResponse reissueToken(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 리프레시 토큰 가져오기
        String refreshToken = CookieUtil.getRefreshToken(request);
        if (refreshToken == null) {
            throw new CustomException(JwtExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // Redis에서 저장된 리프레시 토큰 확인
        String adminId = jwtUtil.getRefreshTokenAdminId(refreshToken);
        String storedRefreshToken = tokenRepository.getRefreshToken(adminId);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new CustomException(JwtExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 리프레시 토큰 유효성 검증
        if (jwtUtil.isRefreshTokenExpired(refreshToken)) {
            throw new CustomException(JwtExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 레디스에 저장된 토큰 검증이 통과한 경우 정상 요청으로 판단, 새 토큰 생성
        String newAccessToken = "Bearer " +jwtUtil.createAccessToken(adminId, jwtUtil.getRefreshTokenRole(refreshToken));
        String newRefreshToken = jwtUtil.createRefreshToken(adminId, jwtUtil.getRefreshTokenRole(refreshToken));

        // 기존 리프레시 토큰 삭제 & 새로운 리프레시 토큰 저장
        tokenRepository.deleteRefreshToken(adminId);
        tokenRepository.saveRefreshToken(adminId, newRefreshToken, 60 * 60 * 24 * 7);

        // 응답 설정
        response.setHeader("Authorization", newAccessToken);
        response.addCookie(CookieUtil.createCookie("Refresh-Token", newRefreshToken, 604800));

//        AdminReissueResponse responseBody = new AdminReissueResponse(Long.parseLong(adminId), newAccessToken);
        return new AdminReissueResponse(Long.parseLong(adminId), newAccessToken);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에서 리프레시 토큰 가져오지만 쿠키 값이 비워져 있는 경우에 대한 부분도 후에 개선 필요함
        String refreshToken = CookieUtil.getRefreshToken(request);
        if (refreshToken == null) {
            throw new CustomException(JwtExceptionCode.EMPTY_REFRESH_TOKEN);
        }

        // Redis에서 저장된 리프레시 토큰 삭제
        String adminId = jwtUtil.getRefreshTokenAdminId(refreshToken);
        tokenRepository.deleteRefreshToken(adminId);

        // 응답에서 토큰 삭제 (쿠키 및 헤더 초기화)
        response.setHeader("Authorization", ""); // 헤더 제거
        CookieUtil.clearAuthCookie(response, "Refresh-Token"); // 쿠키 삭제

    }


//    public AdminLoginResponse login(AdminLoginRequest request) {
//        Admin admin = adminQueryService.findByEmail(request.email());
//        String token = adminCommandService.login(request.email(), request.password());
//        return new AdminLoginResponse(admin.getId(), admin.getEmail(), token);
//    }
}
