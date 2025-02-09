package goorm.deepdive.team1.infra.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// 토큰 생성과 검증 로직을 담은 클래스
@Component
public class JWTUtil {

    private SecretKey Access_secretKey;
    private SecretKey refrsh_secretKey;

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.expiration-seconds}")
    private long expirationSeconds;

    @Value("${spring.jwt.refresh-secret}")
    private String refresh_secret;

    @Value("${spring.jwt.expiration-seconds}")
    private long refresh_expirationSeconds;


    @PostConstruct
    public void init() {
        this.Access_secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.refrsh_secretKey = Keys.hmacShaKeyFor(refresh_secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String adminId, String role) {
        long expirationMs = expirationSeconds * 1000; // 만료시간 (ex expirationSeconds가 600인경우 => 10분)
        return Jwts.builder()
                .claim("AdminId", adminId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(Access_secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String adminId) {
        long expirationMs = refresh_expirationSeconds * 1000; // 7일
        return Jwts.builder()
                .claim("AdminId", adminId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(refrsh_secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Access_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Access_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // Refresh AccessToken이 만료되었는지 확인 */
    public Boolean isExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Access_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // Refresh Token이 만료되었는지 확인 */
    public Boolean isRefreshTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refrsh_secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public void saveRefreshToken(String email, String refreshToken) {
        // Redis 또는 DB에 Refresh Token을 저장하는 로직 추가 가능
    }
}