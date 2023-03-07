package com.project.debby.domain.integrations.minio.service.profileAvatarFactory;

import com.project.debby.domain.integrations.minio.MinioConstants;
import com.project.debby.domain.integrations.minio.model.entity.File;
import com.project.debby.domain.user.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProfileAvatarFactoryImpl implements ProfileAvatarFactory{
    @Override
    public File create(User user) {
        return File
                .builder()
                .bucket(MinioConstants.PROFILE_AVATAR_BUCKET_NAME)
                .filePath(user.getId().toString())
                .minioFileName(UUID.randomUUID())
                .build();
    }
}
