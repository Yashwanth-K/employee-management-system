package com.yash.employeemanagement.security;

// this class handles creating, parsing and validating JWT tokens

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component //Mark this class as a Spring managed bean so it can be injected into other components
public class JwtUtil {
//    Generates a secure HMAC SHA-256 secret key for signing tokens.
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //Use final to make sure the key is immutable | Expiration time for the token, set to 1 hour
    private final long expirationTime = 1000 * 60 * 60; // 1 hour

    //Method to generate a JWT token for a given username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    // check if expirateion date is before current date, if yes, token is expired
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
}
