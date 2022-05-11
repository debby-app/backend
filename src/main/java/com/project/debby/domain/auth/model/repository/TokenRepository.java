package com.project.debby.domain.auth.model.repository;

import com.project.debby.domain.auth.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> getTokenByRefreshToken(String refreshToken);
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM userdetails_token WHERE token_id = :id")
    void deleteFromAssociation(@Param(value = "id") Long id);
}
