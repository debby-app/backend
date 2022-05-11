package com.project.debby.domain.integrations.minio.model.repository;

import com.project.debby.domain.integrations.minio.model.entity.ProfileAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileAvatarRepository extends JpaRepository<ProfileAvatar, Long> {
    Optional<ProfileAvatar> findByMinioFileName(String fileName);
}
