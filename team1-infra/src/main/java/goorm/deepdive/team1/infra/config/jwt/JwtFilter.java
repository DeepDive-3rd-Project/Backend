package goorm.deepdive.team1.infra.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.deepdive.team1.domain.admin.domain.Admin;
import goorm.deepdive.team1.domain.admin.domain.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
//    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    private final JWTUtil jwtUtil;
//    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청에서 액세스 토큰 가져오기
        String accessToken = extractToken(request, ACCESS_TOKEN_HEADER);

        // 액세스 토큰이 없거나 만료된 경우 처리
        if (accessToken == null || jwtUtil.isExpired(accessToken)) {
            log.warn("액세스 토큰이 만료되었거나 없습니다.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, Object> errorResponse = Map.of(
                    "status", 401,
                    "error", "Unauthorized",
                    "message", "엑세스 토큰이 없거나 만료되었습니다.",
                    "path", request.getRequestURI()
            );

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        // 액세스 토큰이 유효하면 인증 객체 설정
        setAuthentication(jwtUtil.getAdminId(accessToken), jwtUtil.getRole(accessToken));
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return uri.contains("login") || uri.contains("swagger-ui") || uri.contains("swagger-resources")
                || uri.contains("v3/api-docs") || uri.contains("webjars")
                || uri.contains("/reissue")
                || (uri.contains("admin") && request.getMethod().equals("POST"))
                || uri.contains("register"); // 회원가입 엔드포인트 추가

    }

    /**
     * 헤더에서 토큰 추출
     */
    private String extractToken(HttpServletRequest request, String headerName) {
        String header = request.getHeader(headerName);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }

//    블랙리스트(로그아웃된) 토큰인지 확인
//    private boolean isTokenBlacklisted(String token) {
//        return Boolean.TRUE.equals(redisTemplate.hasKey("admin:blacklist:refreshToken:" + token));
//    }

//    스프링 시큐리티 인증 객체 설정
    // Spring Security의 인증(Authentication) 객체를 생성하여 현재 요청의 SecurityContextHolder에 저장
    private void setAuthentication(String adminId, String role) {
        Admin adminEntity = Admin.builder()
                .id(Long.valueOf(adminId)) // adminId를 Long으로 변환하여 설정
                .role(Role.valueOf(role)) // Role Enum 설정
                .build();
        CustomAdminDetails adminDetails = new CustomAdminDetails(adminEntity);
        Authentication authToken = new UsernamePasswordAuthenticationToken(adminDetails, null,
                adminDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}