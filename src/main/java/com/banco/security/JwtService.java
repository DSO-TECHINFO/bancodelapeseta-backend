package com.banco.security;

import ch.qos.logback.core.util.TimeUtil;
import com.banco.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Data
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private final String JWT_SECRET;
    @Value("${jwt.expiration}")
    private final String JWT_EXPIRATION;
    @Value("${jwt.refresh.expiration}")
    private final String JWT_REFRESH_EXPIRATION;

    public JwtService(@Value("${jwt.secret}") String JWT_SECRET,
                      @Value("${jwt.expiration}") String JWT_EXPIRATION,
                      @Value("${jwt.refresh.expiration}") String JWT_REFRESH_EXPIRATION) {
        this.JWT_SECRET = JWT_SECRET;
        this.JWT_EXPIRATION = JWT_EXPIRATION;
        this.JWT_REFRESH_EXPIRATION = JWT_REFRESH_EXPIRATION;
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().decryptWith(getSignInKey()).build().parseSignedClaims(token).getPayload();

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder().subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(Long.getLong(JWT_EXPIRATION))))
                .claims(new HashMap<>()).signWith(getSignInKey()).compact();
    }

    public String generateToken(User userDetails) {
        return generateToken(new HashMap<String, Object>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, User userDetails) {
        return Jwts.builder().claims(extraClaims).subject(userDetails.getEmail()).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(Long.getLong(JWT_EXPIRATION)))).signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        return user.getEmail().equals(extractEmail(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return new Date(System.currentTimeMillis()).after(extractClaim(token, Claims::getExpiration));
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
