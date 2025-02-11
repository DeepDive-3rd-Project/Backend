package goorm.deepdive.team1.api.admin.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;


import goorm.deepdive.team1.api.admin.presentation.request.AdminRegisterRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminLoginRequest;
import goorm.deepdive.team1.api.admin.presentation.response.AdminRegisterResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminReissueResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ADMIN", description = "관리자 API")  // Swagger 태그 추가
public interface AdminController {

    @Operation(summary = "관리자 회원가입 API", description = """
            - Description : 이 API는 새로운 관리자를 등록할 수 있습니다.
        """)
    @ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = AdminRegisterResponse.class))
    )
    @PostMapping("/register")
    ResponseEntity<AdminRegisterResponse> register(@Valid @RequestBody AdminRegisterRequest request);

    @Operation(summary = "JWT 재발급 API", description = """
            - Description : 액세스 토큰이 만료되었을 때, 새로운 토큰을 발급받을 수 있습니다.
        """)
    @ApiResponse(
            responseCode = "200",
            description = "토큰 재발급 성공",
            content = @Content(schema = @Schema(implementation = AdminReissueResponse.class))
    )
    @PostMapping("/reissue")
    ResponseEntity<AdminReissueResponse> reissueToken(HttpServletRequest request, HttpServletResponse response);

    @Operation(summary = "관리자 로그아웃 API", description = """
            - Description : 로그아웃을 수행하며, 리프레시 토큰을 삭제하고 세션을 종료합니다.
        """)
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response);
}