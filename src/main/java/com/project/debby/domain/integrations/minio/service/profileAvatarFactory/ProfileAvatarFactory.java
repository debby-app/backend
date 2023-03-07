package com.project.debby.domain.integrations.minio.service.profileAvatarFactory;

import com.project.debby.domain.integrations.minio.model.entity.File;
import com.project.debby.domain.user.model.User;

public interface ProfileAvatarFactory {
    File create(User user);
}
