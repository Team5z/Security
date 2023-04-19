package com.agile.demo.repository;

import com.nimbusds.oauth2.sdk.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public class RefreshTokenRepository implements JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenValue(String value) {
        return;
    }
}