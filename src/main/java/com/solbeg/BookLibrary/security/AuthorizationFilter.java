package com.solbeg.BookLibrary.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static com.solbeg.BookLibrary.utils.LibraryConstants.JWT_PREFIX;
import static com.solbeg.BookLibrary.utils.LibraryConstants.JWT_SECRET;
import static com.solbeg.BookLibrary.utils.LibraryConstants.LOGIN_URL;
import static com.solbeg.BookLibrary.utils.LibraryConstants.REFRESH_TOKEN_URL;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ROLES;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals(LOGIN_URL) || request.getServletPath().equals(REFRESH_TOKEN_URL)) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(JWT_PREFIX)) {
                try {
                    String token = authorizationHeader.substring(JWT_PREFIX.length());
                    Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(decodedJWT);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    Map<String, String> error = JWTResponseService.responseErrorConfiguration(response, exception);
                    JWTResponseService.configurationJSONResponse(response, error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(DecodedJWT decodedJWT) {
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim(ROLES).asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
