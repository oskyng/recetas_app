package com.duoc.seguridad_calidad.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    // clave de 64 bytes (hex string de application.properties)
    private final String secret = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";

    private Key signingKey;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);
        signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Test
    void extractUsername_withValidToken_returnsSubject() {
        String token = Jwts.builder()
                .setSubject("alice")
                .setIssuedAt(new Date())
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        assertEquals("alice", jwtService.extractUsername(token));
    }

    @Test
    void isTokenValid_trueWhenUsernameMatchesAndNotExpired() {
        Date now = new Date();
        String token = Jwts.builder()
                .setSubject("alice")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 10_000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        UserDetails userDetails = new User("alice", "pwd", Collections.emptyList());
        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void expiredToken_operationsThrowExpiredJwtException() {
        Date now = new Date();
        String token = Jwts.builder()
                .setSubject("bob")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() - 1_000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> jwtService.isTokenExpired(token));
        UserDetails userDetails = new User("bob", "pwd", Collections.emptyList());
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> jwtService.isTokenValid(token, userDetails));
    }
}
