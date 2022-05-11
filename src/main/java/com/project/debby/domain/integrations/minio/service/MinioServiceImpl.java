package com.project.debby.domain.integrations.minio.service;

import com.project.debby.domain.integrations.minio.MinioConstants;
import com.project.debby.domain.integrations.minio.client.MinioClientEx;
import com.project.debby.domain.integrations.minio.client.dto.GetFileURLMinioDTO;
import com.project.debby.domain.integrations.minio.client.dto.UploadFileMinioDTO;
import com.project.debby.domain.integrations.minio.model.entity.ProfileAvatar;
import com.project.debby.domain.integrations.minio.service.profileAvatarFactory.ProfileAvatarFactory;
import com.project.debby.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RequiredArgsConstructor
@Service
public class MinioServiceImpl implements MinioService {

    private final MinioClientEx clientEx;
    private final ProfileAvatarFactory avatarFactory;

    @SneakyThrows
    public void initDefaultBucket() {
        if (!clientEx.checkBucketExist(MinioConstants.PROFILE_AVATAR_BUCKET_NAME)) {
            clientEx.createBucket(MinioConstants.PROFILE_AVATAR_BUCKET_NAME); // hm, maybe find best logic?
        }
    }

    @Override
    public String getAvatarURL(User user) {
        var avatar = user.getAvatar();
        if (avatar == null) {
            log.info("User {} don't have avatar", user.getUsername());
            return "";
        }
        try {
            log.info("Start getting avatar link for user {}", user.getUsername());
            return clientEx.getFileURL(GetFileURLMinioDTO.create(avatar));
        } catch (Exception e) {
            log.error("Cannot get avatar for user {}, reason {}", user.getUsername(), e.getMessage());
            return "";
        }
    }

    @Override
    public ProfileAvatar saveUserAvatar(User user, MultipartFile avatarFile) {
        log.info("Start create new avatar for user {}", user.getUsername());
        var avatar = avatarFactory.create(user);

        try {
            clientEx.saveFile(
                    UploadFileMinioDTO.create(avatar, avatarFile.getInputStream(), avatarFile.getContentType())
            );
            return avatar;
        } catch (Exception e) {
            log.error("Cannot create avatar for user {}. reason {}", user.getUsername(), e.getMessage());
            return null; //hm, exception doesn't exist
        }
    }
}
