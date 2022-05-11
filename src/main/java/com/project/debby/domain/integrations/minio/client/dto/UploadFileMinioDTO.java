package com.project.debby.domain.integrations.minio.client.dto;

import com.project.debby.domain.integrations.minio.model.entity.ProfileAvatar;
import lombok.Data;

import java.io.InputStream;
import java.util.UUID;

@Data
public class UploadFileMinioDTO {
    private String bucketName;
    private String filePath;
    private UUID fileName;
    private String mimeType;
    private InputStream file;

    private UploadFileMinioDTO() {
    }

    private UploadFileMinioDTO(String bucketName, String filePath, UUID fileName, String mimeType, InputStream file) {
        this.bucketName = bucketName;
        this.filePath = filePath;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.file = file;
    }

    public static UploadFileMinioDTO create(ProfileAvatar profileAvatar,
                                            InputStream inputStream,
                                            String mimeType){
        return new UploadFileMinioDTO(
                profileAvatar.getBucket(),
                profileAvatar.getFilePath(),
                profileAvatar.getMinioFileName(),
                mimeType,
                inputStream
        );

    }
}
