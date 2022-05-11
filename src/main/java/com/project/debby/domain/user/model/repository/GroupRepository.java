package com.project.debby.domain.user.model.repository;

import com.project.debby.domain.user.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> getAllByOwner_UserDetails_Credentials_ExternalId(String externalId);
    Optional<Group> findByNameAndOwner_UserDetails_Credentials_ExternalId(String name, String userID);
}
