package com.project.quiz_system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService{
    private static final String SECRET="mysecretkeymysecretkeymysecretkeymysecretkey";

    private final Key key=Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis()+1000*60*60)
                )
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token){
        Claims claims=Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean isTokenValid(String token,String email){
        String extractedEmail=extractEmail(token);

        return extractedEmail.equals(email) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token){
        Date expiration=Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}