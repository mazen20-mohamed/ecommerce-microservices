package com.example.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RefreshScope
public class JwtUtil {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64
                .decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /******************* extract all claims ******************/
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /******************* extract specific claim from claims ******************/
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /******************* User Name ******************/
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /******************* Expiration Date ******************/
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /******************* check token is expired ******************/
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

}
