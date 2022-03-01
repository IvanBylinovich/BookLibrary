package com.solbeg.BookLibrary.security;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.solbeg.BookLibrary.utils.LibraryConstants.HEADER_PASSWORD;
import static com.solbeg.BookLibrary.utils.LibraryConstants.HEADER_USERNAME;
import static com.solbeg.BookLibrary.utils.LibraryConstants.JWT_SECRET;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter(HEADER_USERNAME);
        String password = request.getParameter(HEADER_PASSWORD);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes());
        String access_token = jwtService.createAccessToken(user, request, algorithm);
        String refresh_token = jwtService.createRefreshToken(user, request, algorithm);
        Map<String, String> tokens = jwtService.mapTokens(access_token, refresh_token);
        JWTResponseService.configurationJSONResponse(response, tokens);
    }
}
