package goorm.deepdive.team1.api.admin.presentation;

import goorm.deepdive.team1.api.admin.application.AdminFacade;
import goorm.deepdive.team1.api.admin.presentation.request.AdminLoginRequest;
import goorm.deepdive.team1.api.admin.presentation.request.AdminRegisterRequest;
import goorm.deepdive.team1.api.admin.presentation.response.AdminLoginResponse;
import goorm.deepdive.team1.api.admin.presentation.response.AdminRegisterResponse;
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

    @PostMapping("/register")
    public ResponseEntity<AdminRegisterResponse> register(@RequestBody AdminRegisterRequest request) {
        System.out.println("Request received: " + request);
        AdminRegisterResponse response = adminFacade.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest request) {
        AdminLoginResponse response = adminFacade.login(request);
        return ResponseEntity.ok(response);
    }


}
