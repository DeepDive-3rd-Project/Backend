package goorm.deepdive.team1.infra.config.security;

import goorm.deepdive.team1.domain.admin.security.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private final Key signingKey;
    private final long expirationMillis;

    public JwtTokenProviderImpl(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.expiration-seconds}") long expirationSeconds
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMillis = expirationSeconds * 1000L;
    }

    @Override
    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}