package com.project.debby.domain.integrations.minio.client.dto;

import com.project.debby.domain.integrations.minio.model.entity.File;
import lombok.Data;

@Data
public class GetFileURLMinioDTO {
    String bucketName;
    String filePath;
    String fileName;

    private GetFileURLMinioDTO() {
    }

    private GetFileURLMinioDTO(String bucketName, String filePath, String fileName) {
        this.bucketName = bucketName;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public static GetFileURLMinioDTO create(File avatar) {
        return new GetFileURLMinioDTO(
                avatar.getBucket(),
                avatar.getFilePath(),
                avatar.getMinioFileName().toString()
        );
    }
}
