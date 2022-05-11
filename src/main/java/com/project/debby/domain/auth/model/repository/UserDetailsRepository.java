package com.project.debby.domain.auth.model.repository;

import com.project.debby.domain.auth.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByCredentials_ExternalId(String externalId);
    Optional<UserDetails> findByCredentials_Email(String email);
}
