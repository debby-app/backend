package com.project.debby.domain.integrations.minio.service;

import com.project.debby.domain.integrations.minio.MinioConstants;
import com.project.debby.domain.integrations.minio.client.MinioClientEx;
import com.project.debby.domain.integrations.minio.client.dto.GetFileURLMinioDTO;
import com.project.debby.domain.integrations.minio.client.dto.UploadFileMinioDTO;
import com.project.debby.domain.integrations.minio.client.exception.CannotRemoveObjectMinioException;
import com.project.debby.domain.integrations.minio.model.entity.File;
import com.project.debby.domain.integrations.minio.service.fileFactory.FileFactory;
import com.project.debby.domain.integrations.minio.service.profileAvatarFactory.ProfileAvatarFactory;
import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;


@Log4j2
@RequiredArgsConstructor
@Service
public class MinioServiceImpl implements MinioService {

    private final MinioClientEx clientEx;
    private final ProfileAvatarFactory avatarFactory;
    private final FileFactory fileFactory;

    @PostConstruct
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
    public File saveUserAvatar(User user, MultipartFile avatarFile) {
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

    @Override
    public File saveImage(LoanState state, MultipartFile file) {
        log.info("Start create new image {}", file.getOriginalFilename());
        var filed = fileFactory.create(state);
        try {
            clientEx.saveFile(
                    UploadFileMinioDTO.create(filed, file.getInputStream(), file.getContentType())
            );
            return filed;
        } catch (Exception e) {
            log.error("Cannot create file for loan state {}. reason {}", state.getId(), e.getMessage());
            return null; //hm, exception doesn't exist
        }
    }

    @Override
    public String getImageURL(LoanState state) {
        try {
            log.info("Start getting image link for loan state {}", state.getId());
            return clientEx.getFileURL(GetFileURLMinioDTO.create(state.getFile()));
        } catch (Exception e) {
            log.error("Cannot get avatar for loan state {}, reason {}", state.getId(), e.getMessage());
            return "";
        }
    }

    @Override
    public void removeImage(LoanState state) throws CannotRemoveObjectMinioException {
        log.info("Start delete image for state {}", state.getId());
        var filed = fileFactory.create(state);
        try {
            clientEx.deleteFile(GetFileURLMinioDTO.create(filed));
        } catch (Exception e) {
            log.error("Cannot remove file for loan state {}. reason {}", state.getId(), e.getMessage());
        }
    }
}
