package com.solbeg.BookLibrary.config;

import com.solbeg.BookLibrary.model.Authority;
import com.solbeg.BookLibrary.security.AuthenticationFilter;
import com.solbeg.BookLibrary.security.AuthorizationFilter;
import com.solbeg.BookLibrary.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.solbeg.BookLibrary.utils.LibraryConstants.ENDPOINTS_ADMIN_AUTHORITY_DELETE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ENDPOINTS_ADMIN_AUTHORITY_EDIT;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ENDPOINTS_ADMIN_AUTHORITY_PATCH;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ENDPOINTS_ADMIN_AUTHORITY_READ;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ENDPOINTS_ADMIN_AUTHORITY_WRITE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ENDPOINTS_AUTHORIZATION_WHITELIST;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ENDPOINTS_USER_AUTHORITY_DELETE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ENDPOINTS_USER_AUTHORITY_EDIT;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ENDPOINTS_USER_AUTHORITY_READ;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ENDPOINTS_USER_AUTHORITY_WRITE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WabSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTService jwtService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(jwtService, authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/login");
        http.
                csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests().antMatchers(ENDPOINTS_AUTHORIZATION_WHITELIST).permitAll()
                .antMatchers(HttpMethod.GET, ENDPOINTS_USER_AUTHORITY_READ).hasAnyAuthority(Authority.READ.name())
                .antMatchers(HttpMethod.POST, ENDPOINTS_USER_AUTHORITY_WRITE).hasAnyAuthority(Authority.WRITE.name())
                .antMatchers(HttpMethod.PUT, ENDPOINTS_USER_AUTHORITY_EDIT).hasAnyAuthority(Authority.EDIT.name())
                .antMatchers(HttpMethod.DELETE, ENDPOINTS_USER_AUTHORITY_DELETE).hasAnyAuthority(Authority.DELETE.name())
                .antMatchers(HttpMethod.GET, ENDPOINTS_ADMIN_AUTHORITY_READ).hasAnyAuthority(Authority.ALL.name())
                .antMatchers(HttpMethod.POST, ENDPOINTS_ADMIN_AUTHORITY_WRITE).hasAnyAuthority(Authority.ALL.name())
                .antMatchers(HttpMethod.PUT, ENDPOINTS_ADMIN_AUTHORITY_EDIT).hasAnyAuthority(Authority.ALL.name())
                .antMatchers(HttpMethod.PATCH, ENDPOINTS_ADMIN_AUTHORITY_PATCH).hasAnyAuthority(Authority.ALL.name())
                .antMatchers(HttpMethod.DELETE, ENDPOINTS_ADMIN_AUTHORITY_DELETE).hasAnyAuthority(Authority.ALL.name())
                .anyRequest().authenticated()
                .and()
                .addFilter(authenticationFilter)
                .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
