package com.banco.security;

import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.ContentType;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AccountVerifiedFilter extends OncePerRequestFilter {
    @Value("${endpoints.noauth}")
    private List<String> noAuthEndpoints;
    @Value("${enpoints.notverified}")
    private List<String> notVerifiedEndpoints;
    @Autowired
    private EntityRepository entityRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestServletPath = request.getRequestURI().replace("/api/v1","");
        if(noAuthEndpoints.stream().noneMatch(endpoint -> endpoint.equals(requestServletPath)) && notVerifiedEndpoints.stream().noneMatch(endpoint -> endpoint.equals(requestServletPath))){
            String token = request.getHeader("Authorization");
            token = token.replace("Bearer ", "");
            String taxId = jwtService.extractTaxId(token);
            Optional<Entity> user = entityRepository.findByTaxId(taxId);
            Entity userPresent;
            if(user.isEmpty())
                return;
            userPresent = user.get();
            if(userPresent.getEmailConfirmed() && userPresent.getPhoneConfirmed())
                filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request,response);
    }
}
