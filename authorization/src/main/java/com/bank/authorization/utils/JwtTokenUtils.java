package com.bank.authorization.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

import static com.bank.authorization.utils.ApplicationConstants.*;

@Component
@Slf4j
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    public String generateToken(UserDetails userDetails) {
        log.info("Генерация токена для пользователя {}", userDetails.getUsername());
        return JWT.create()
                .withClaim(CLAIM_ROLE, userDetails.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).findFirst().orElse("ROLE_USER"))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtLifetime.toMillis()))
                .withSubject(userDetails.getUsername())
                .sign(Algorithm.HMAC256(secret));

    }

    private DecodedJWT verify(String token) {
        log.info("Верификация JWT токена");
        try {
            DecodedJWT decodedJWT = JWT
                    .require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
            log.info("Токен {} успешно верифицирован", decodedJWT.getSubject());
            return decodedJWT;
        } catch (JWTVerificationException e) {
            log.error("Ошибка верификации токена", e);
            throw e;
        }


    }

    public String getUsername(String token) {
        return verify(token).getSubject();
    }

    public String getRole(String token) {
        return verify(token)
                .getClaim(CLAIM_ROLE)
                .asString();
    }

    public Date getExpiresDate(String token) {
        return verify(token)
                .getExpiresAt();
    }


}
