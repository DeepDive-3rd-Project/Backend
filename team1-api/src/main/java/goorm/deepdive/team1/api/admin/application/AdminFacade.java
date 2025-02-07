package goorm.deepdive.team1.api.admin.application;

import goorm.deepdive.team1.api.admin.presentation.request.AdminLoginRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRegisterRequest;
import goorm.deepdive.team1.api.admin.presentation.response.AdminLoginResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminRegisterResponse;
import goorm.deepdive.team1.domain.admin.application.AdminCommandService;
import goorm.deepdive.team1.domain.admin.application.AdminQueryService;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminFacade {
    private final AdminCommandService adminCommandService;
    private final AdminQueryService adminQueryService;

    public AdminRegisterResponse register(AdminRegisterRequest request) {
        Admin admin = adminCommandService.register(request.email(), request.password(), request.role());
        String token = adminCommandService.login(request.email(), request.password());
        return new AdminRegisterResponse(admin.getId(), admin.getEmail(), token);
    }

    public AdminLoginResponse login(AdminLoginRequest request) {
        Admin admin = adminQueryService.findByEmail(request.email());
        String token = adminCommandService.login(request.email(), request.password());
        return new AdminLoginResponse(admin.getId(), admin.getEmail(), token);
    }
}
