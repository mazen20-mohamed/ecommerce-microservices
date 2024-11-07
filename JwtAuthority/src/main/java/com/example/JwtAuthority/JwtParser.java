package com.example.jwtAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Collection;
import java.util.List;

public class JwtParser {

    Claims claims;

    public JwtParser(String token,String secretKey){
        this.claims = parseJwt(token,secretKey);
    }

    public Claims parseJwt(String token, String secretToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(secretToken))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Collection<? extends GrantedAuthority> getUserAuthority(){
        return List.of(new SimpleGrantedAuthority(claims.get("role").toString()));
    }

    public String getUserId(){
        return claims.get("id").toString();
    }

    public String getSubject(){
        return claims.getSubject();
    }
}
