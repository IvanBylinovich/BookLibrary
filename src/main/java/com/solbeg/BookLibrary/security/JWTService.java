package com.solbeg.BookLibrary.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.solbeg.BookLibrary.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JWTService {

    public UserDetails getRequestUser(Algorithm algorithm, String refresh_token, UserService userService) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        String username = decodedJWT.getSubject();
        return userService.findUserByUsernameOrThrowException(username);
    }

    public String createAccessToken(UserDetails user, HttpServletRequest request, Algorithm algorithm) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                .withIssuer(request.getRequestURI())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String createRefreshToken(org.springframework.security.core.userdetails.User user, HttpServletRequest request, Algorithm algorithm) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 100 * 60 * 1000))
                .withIssuer(request.getRequestURI())
                .sign(algorithm);
    }

    public Map<String, String> mapTokens(String access_token, String refresh_token) {
        return Map.of("access_token", access_token, "refresh_token", refresh_token);
    }
}
