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

import static com.solbeg.BookLibrary.utils.LibraryConstants.HEADER_ACCESS_TOKEN;
import static com.solbeg.BookLibrary.utils.LibraryConstants.HEADER_REFRESH_TOKEN;
import static com.solbeg.BookLibrary.utils.LibraryConstants.JWT_REFRESH_TOKEN_LIFETIME;
import static com.solbeg.BookLibrary.utils.LibraryConstants.JWT_TOKEN_LIFETIME;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ROLES;

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
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_LIFETIME))
                .withIssuer(request.getRequestURI())
                .withClaim(ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String createRefreshToken(org.springframework.security.core.userdetails.User user, HttpServletRequest request, Algorithm algorithm) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_LIFETIME))
                .withIssuer(request.getRequestURI())
                .sign(algorithm);
    }

    public Map<String, String> mapTokens(String access_token, String refresh_token) {
        return Map.of(HEADER_ACCESS_TOKEN, access_token, HEADER_REFRESH_TOKEN, refresh_token);
    }
}
