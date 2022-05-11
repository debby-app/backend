package com.project.debby.domain.integrations.minio.client;

import com.project.debby.domain.integrations.minio.client.dto.GetFileURLMinioDTO;
import com.project.debby.domain.integrations.minio.client.dto.UploadFileMinioDTO;
import com.project.debby.domain.integrations.minio.client.exception.CannotCheckBucketInMinioException;
import com.project.debby.domain.integrations.minio.client.exception.CannotCreateBucketInMinioException;
import com.project.debby.domain.integrations.minio.client.exception.CannotGetFileURLFromMinioException;
import com.project.debby.domain.integrations.minio.client.exception.CannotUploadFileToMinioException;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class MinioClientExImpl implements MinioClientEx {

    private final MinioClient minioClient;

    @Override
    public String getFileURL(GetFileURLMinioDTO minioDTO) throws CannotGetFileURLFromMinioException {
        var request = GetPresignedObjectUrlArgs.builder()
                .expiry(2, TimeUnit.DAYS)
                .method(Method.GET)
                .bucket(minioDTO.getBucketName())
                .object(getFileFullPath(minioDTO.getFilePath(), minioDTO.getFileName()))
                .build();

        try {
            return minioClient.getPresignedObjectUrl(request);
        } catch (Exception e) {
            throw new CannotGetFileURLFromMinioException(e.getMessage());
        }
    }

    @Override
    public void saveFile(UploadFileMinioDTO minioDTO) throws CannotUploadFileToMinioException {
        var uploadObjectRequest = PutObjectArgs
                .builder()
                .bucket(minioDTO.getBucketName())
                .object(getFileFullPath(minioDTO.getFilePath(), minioDTO.getFileName().toString()))
                .stream(minioDTO.getFile(), -1, 10485760)
                .build();

        try {
            minioClient.putObject(uploadObjectRequest);
        } catch (Exception e) {
            throw new CannotUploadFileToMinioException(e.getMessage());
        }
    }

    @Override
    public void createBucket(String bucketName) throws CannotCreateBucketInMinioException {
        var createRootBucketArgs = MakeBucketArgs
                .builder()
                .bucket(bucketName)
                .build();

        try {
            minioClient.makeBucket(createRootBucketArgs);
        } catch (Exception e) {
            throw new CannotCreateBucketInMinioException(e.getMessage());
        }
    }

    @Override
    public Boolean checkBucketExist(String bucketName) throws CannotCheckBucketInMinioException {
        var checkBucketExistRequest = BucketExistsArgs
                .builder()
                .bucket(bucketName)
                .build();

        try {
            return minioClient.bucketExists(checkBucketExistRequest);
        } catch (Exception e){
            throw new CannotCheckBucketInMinioException(e.getMessage());
        }
    }

    private String getFileFullPath(String path, String fileName) {
        return path + fileName;
    }
}
