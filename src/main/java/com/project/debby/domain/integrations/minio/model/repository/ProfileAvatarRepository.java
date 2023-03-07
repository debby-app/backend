package com.project.debby.domain.integrations.minio.model.repository;

import com.project.debby.domain.integrations.minio.model.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileAvatarRepository extends JpaRepository<File, Long> {
    Optional<File> findByMinioFileName(String fileName);
}
