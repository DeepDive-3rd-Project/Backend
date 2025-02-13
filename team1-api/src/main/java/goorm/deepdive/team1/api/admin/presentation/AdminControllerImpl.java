package goorm.deepdive.team1.api.admin.presentation;

import goorm.deepdive.team1.api.admin.application.AdminFacade;
import goorm.deepdive.team1.api.admin.presentation.request.AdminLoginRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRegisterRequest;
import goorm.deepdive.team1.api.admin.presentation.response.AdminLoginResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminRegisterResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminReissueResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController{
    private final AdminFacade adminFacade;

    @Override
    @PostMapping("/register")
    public ResponseEntity<AdminRegisterResponse> register(@RequestBody AdminRegisterRequest request) {
        AdminRegisterResponse response = adminFacade.register(request);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AdminLoginRequest request) {
        return ResponseEntity.ok().build();
    }

    // JWT 재발급 API
    // 엑세스 토큰이 만료되었을 경우, 401 응답을 반환하고 클라이언트가 /reissue API를 호출한다고 가정
    @Override
    @PostMapping("/reissue")
    public ResponseEntity<AdminReissueResponse> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        AdminReissueResponse registerResponse = adminFacade.reissueToken(request, response);
        return ResponseEntity.ok(registerResponse);
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        adminFacade.logout(request, response);
        return ResponseEntity.ok().build();
    }



}
