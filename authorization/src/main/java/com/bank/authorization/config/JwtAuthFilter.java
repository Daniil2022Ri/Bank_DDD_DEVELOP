package com.bank.authorization.config;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bank.authorization.exceptions.CustomSignatureVerificationException;
import com.bank.authorization.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static com.bank.authorization.utils.ApplicationConstants.AUTHORIZATION_HEADER;
import static com.bank.authorization.utils.ApplicationConstants.BEARER;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        String jwt = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith(BEARER)) {
            jwt = authHeader.substring(7);
            try {
                username = jwtTokenUtils.getUsername(jwt);
            } catch (TokenExpiredException e) {
                throw new TokenExpiredException("Срок действия токена истек",
                        jwtTokenUtils.getExpiresDate(jwt).toInstant());

            } catch (SignatureVerificationException exception) {
                throw new CustomSignatureVerificationException("Неверная подпись");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(jwtTokenUtils.getRole(jwt))));
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}
