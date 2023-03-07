package com.project.debby.domain.integrations.minio.service.fileFactory;

import com.project.debby.domain.integrations.minio.MinioConstants;
import com.project.debby.domain.integrations.minio.model.entity.File;
import com.project.debby.domain.loan.model.LoanState;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class FileFactoryImpl implements FileFactory {
    @Override
    public File create(LoanState state) {
        return File
                .builder()
                .bucket(MinioConstants.PROFILE_AVATAR_BUCKET_NAME)
                .filePath(state.getId().toString())
                .minioFileName(UUID.randomUUID())
                .build();
    }
}
