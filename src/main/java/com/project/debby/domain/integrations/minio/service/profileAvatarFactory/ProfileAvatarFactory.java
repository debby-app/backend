package com.project.debby.domain.integrations.minio.service.profileAvatarFactory;

import com.project.debby.domain.integrations.minio.model.entity.ProfileAvatar;
import com.project.debby.domain.user.model.User;

public interface ProfileAvatarFactory {
    ProfileAvatar create(User user);
}
