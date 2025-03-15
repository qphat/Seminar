package com.koomi.seminar2.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Remove "Bearer " prefix

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                for (String authority : List.of(authorities.split(","))) {
                    GrantedAuthority grantedAuthority = () -> authority;
                    grantedAuthorityList.add(grantedAuthority);
                }

                Authentication auth = new UsernamePasswordAuthenticationToken(email, null, grantedAuthorityList);

                SecurityContextHolder.getContext().setAuthentication(auth);

                // Continue the filter chain
                filterChain.doFilter(request, response);

            } catch (Exception e) {
                throw new RuntimeException("Invalid token...", e);
            }
        } else {
            // If no JWT, continue the filter chain
            filterChain.doFilter(request, response);
        }
    }
}