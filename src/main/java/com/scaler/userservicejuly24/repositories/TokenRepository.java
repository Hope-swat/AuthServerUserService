package com.scaler.userservicejuly24.repositories;

import com.scaler.userservicejuly24.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Override
    Token save(Token token);

    Optional<Token> findByValueAndDeleted(String token, boolean deleted);

    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String token, boolean deleted, Date expiryAt);
}
