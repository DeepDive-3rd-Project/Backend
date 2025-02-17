package goorm.deepdive.team1.api.admin.presentation;

import goorm.deepdive.team1.api.admin.application.AdminFacade;
import goorm.deepdive.team1.api.admin.presentation.request.AdminLoginRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRegisterRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminPasswordUpdateRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRoleUpdateRequest;
import goorm.deepdive.team1.api.admin.presentation.response.AdminListResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminRegisterResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminReissueResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminSearchResponse;
import goorm.deepdive.team1.api.security.CustomAdminDetails;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId) {
        adminFacade.deleteAdmin(adminId);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    @Override
    @PatchMapping("/{adminId}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long adminId,
            @RequestBody AdminPasswordUpdateRequest request) {
        adminFacade.updatePassword(adminId, request);
        return ResponseEntity.ok().build(); // 200 OK 반환
    }

    @GetMapping("/list")
    public ResponseEntity<List<AdminListResponse>> getAllAdmins() {
        List<AdminListResponse> adminList = adminFacade.getAllAdmins()
                .stream()
                .map(AdminListResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(adminList);
    }

    @GetMapping("/list/page")
    public ResponseEntity<Page<AdminListResponse>> getAdminsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AdminListResponse> adminPage = adminFacade.getAdminsByPage(page, size)
                .map(AdminListResponse::fromEntity);
        return ResponseEntity.ok(adminPage);
    }

    @GetMapping("/search")
    public ResponseEntity<AdminSearchResponse> getAdminByEmail(@RequestParam String email) {
        Admin admin = adminFacade.getAdminByEmail(email);
        return ResponseEntity.ok(AdminSearchResponse.fromEntity(admin));
    }

    @PatchMapping("/{adminId}/role")
    @PreAuthorize("hasAuthority('SUPER')")
    public ResponseEntity<Void> updateAdminRole(
            @PathVariable Long adminId,
            @RequestBody AdminRoleUpdateRequest request,
            @AuthenticationPrincipal CustomAdminDetails adminDetails // 현재 로그인한 관리자 정보
    ) {

        adminFacade.updateAdminRole(adminId, request,  adminDetails.getAdminId());
        return ResponseEntity.ok().build();
    }
}
