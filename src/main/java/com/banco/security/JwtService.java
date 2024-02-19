package com.banco.security;

import com.banco.entities.Entity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.AeadAlgorithm;
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
    private final Long JWT_EXPIRATION;


    public JwtService(@Value("${jwt.secret}") String JWT_SECRET,
                      @Value("${jwt.expiration}") Long JWT_EXPIRATION) {
        this.JWT_SECRET = JWT_SECRET;
        this.JWT_EXPIRATION = JWT_EXPIRATION;
    }

    public String extractTaxId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String generateToken(Entity entityDetails) {
        return generateToken(new HashMap<String, Object>(), entityDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, Entity entityDetails) {
        return Jwts.builder().claims(extraClaims).subject(entityDetails.getTaxId()).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(JWT_EXPIRATION))).signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Entity entity = (Entity) userDetails;
        return entity.getTaxId().equals(extractTaxId(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return new Date(System.currentTimeMillis()).after(extractClaim(token, Claims::getExpiration));
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
