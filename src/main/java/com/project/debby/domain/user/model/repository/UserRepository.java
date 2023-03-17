package com.project.debby.domain.user.model.repository;

import com.project.debby.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserDetails_Credentials_ExternalId(String userID);
    Optional<User> findByUserDetails_Credentials_Email(String email);
    List<User> getAllByUsernameContains(String username);
}
