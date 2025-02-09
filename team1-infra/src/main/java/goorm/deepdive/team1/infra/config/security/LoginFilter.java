package goorm.deepdive.team1.infra.config.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.deepdive.team1.infra.config.jwt.CustomAdminDetails;
import goorm.deepdive.team1.infra.config.jwt.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/admin/login"); // 인증 URL 설정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);
            String email = credentials.get("email");
            String password = credentials.get("password");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            return authenticationManager.authenticate(authToken);
        } catch (Exception e) {
            throw new RuntimeException("로그인 요청 파싱 오류", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomAdminDetails adminDetails = (CustomAdminDetails) authentication.getPrincipal();
        String email = adminDetails.getUsername(); // enail을 getUsername로 한 이유는 getUsername이 정해진 메서드명입니다
        Long adminId = adminDetails.getAdminId();
        String role = adminDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority())
                .orElse("ROLE_USER");

        // JWT 생성
        String token = jwtUtil.createToken(email, role); // 1시간 유효기간
        response.addHeader("Authorization", "Bearer " + token);
        // JSON 응답 생성
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, String> AdminResponse = new HashMap<>();
            AdminResponse.put("message", "Login successful");
            AdminResponse.put("id", String.valueOf(adminId));
            AdminResponse.put("email", email);
            AdminResponse.put("Accesstoken",token );

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getOutputStream(), AdminResponse);
        } catch (Exception e) {
            throw new RuntimeException("응답 생성 중 오류 발생", e);
        }
    }

//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException{
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401 에러
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
//        errorResponse.put("message", "ID 또는 비밀번호가 일치하지 않습니다.");
//        errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//
//        ObjectMapper mapper = new ObjectMapper();
//        try (PrintWriter writer = response.getWriter()) {
//            writer.write(mapper.writeValueAsString(errorResponse));
//            writer.flush();
//            response.flushBuffer();
//        }
//    }
}



