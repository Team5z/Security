package com.agile.demo.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Date;
import java.util.List;

// import static jdk.nashorn.internal.objects.NativeSet.keys;

public class JwtTokenTest {
   @Autowired
     JwtTokenTest jwtTokenTest;

    @Value(value = "${jwt.secretKey}")
    String accessSecret;
    public final Long ACCESS_TOKEN_EXPIRE_COUNT = 10 * 60 * 1000L;

    @Test
    public void createToken() throws Exception {
        String email = "team5zozoz@gmail.com";
        List<String> roles = List.of("ROLE_USER");
        Long id = 1L;
        Claims claims = Jwts.claims().setSubject(email);

        claims.put("roles", roles);
        claims.put("userId", id);

        byte[] accessSecret = this.accessSecret.getBytes(StandardCharsets.UTF_8);

        String JwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() * this.ACCESS_TOKEN_EXPIRE_COUNT))
                .signWith(Keys.hmacShaKeyFor(accessSecret))`
                .compact();
        System.out.println(JwtToken);
    }

    @Test
            public void ParseToken() throws Exception {
        byte[] accessSecret = this.accessSecret.getBytes(StandardCharsets.UTF_8);

        String JwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + this.ACCESS_TOKEN_EXPIRE_COUNT))
                .signWith(keys.hmacShaKeyFor(accessSecret))
                .compact()
                ;
        System.out.println(JwtToken);

        }

}
