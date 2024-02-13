package com.banco.security;

import io.jsonwebtoken.lang.Strings;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    private final AuthenticationProvider authenticationProvider;
    private final Filter jwtAuthFilter;

    @Value("${endpoints.noauth}")
    private final List<String> NO_AUTH_ENDPOINTS;

    @Value("${endpoints.cashier}")
    private final List<String> CASHIER_ENPOINTS;

    @Value("${endpoints.employee}")
    private final List<String> EMPLOYEE_ENDPOINTS;
    @Value("${endpoints.tpv}")
    private final List<String> TPV_ENDPOINTS;
    @Value("${endpoints.admin}")
    private final List<String> ADMIN_ENDPOINTS;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) -> {
                    authorizeHttpRequests.requestMatchers(Strings.toStringArray(NO_AUTH_ENDPOINTS)).permitAll();
                    authorizeHttpRequests.requestMatchers(Strings.toStringArray(CASHIER_ENPOINTS)).hasRole("CASHIER");
                    authorizeHttpRequests.requestMatchers(Strings.toStringArray(EMPLOYEE_ENDPOINTS)).hasRole("EMPLOYEE");
                    authorizeHttpRequests.requestMatchers(Strings.toStringArray(TPV_ENDPOINTS)).hasRole("TPV");
                    authorizeHttpRequests.requestMatchers(Strings.toStringArray(ADMIN_ENDPOINTS)).hasRole("ADMIN");
                    authorizeHttpRequests.anyRequest().hasAnyRole("USER", "ADMIN");

                }).sessionManagement(
                        (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendError(403);
                }));

        return http.build();

    }

}

