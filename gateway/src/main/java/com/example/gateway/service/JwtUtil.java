package com.example.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String  secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

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
