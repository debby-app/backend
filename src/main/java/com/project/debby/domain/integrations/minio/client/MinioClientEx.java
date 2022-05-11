package com.project.debby.domain.integrations.minio.client;

import com.project.debby.domain.integrations.minio.client.dto.GetFileURLMinioDTO;
import com.project.debby.domain.integrations.minio.client.dto.UploadFileMinioDTO;
import com.project.debby.domain.integrations.minio.client.exception.CannotCheckBucketInMinioException;
import com.project.debby.domain.integrations.minio.client.exception.CannotCreateBucketInMinioException;
import com.project.debby.domain.integrations.minio.client.exception.CannotGetFileURLFromMinioException;
import com.project.debby.domain.integrations.minio.client.exception.CannotUploadFileToMinioException;

/**
 * Данный клиент представляет низкоуровневое апи для общение с минио сервисом
 */
public interface MinioClientEx {
    /**
     * Метод для получения временной ссылки на существующее изображение
     * @param minioDTO dto в котором содержатся все нужные данные
     *                 для получения ссылки ( бакет, путь в бакете, имя )
     * @return url изображе
     */
    String getFileURL(GetFileURLMinioDTO minioDTO) throws CannotGetFileURLFromMinioException;

    /**
     * метод для сохранения файла в minio
     * @param minioDTO дто в котором содержаться все нужные данные
     *                 для сохранения файла (бакет, путь, имя, сам файл)
     */
    void saveFile(UploadFileMinioDTO minioDTO) throws CannotUploadFileToMinioException;

    /**
     * Метод для создание нового бакета в minio
     * @param bucketName имя бакета
     */
    void createBucket(String bucketName) throws CannotCreateBucketInMinioException;

    /**
     * метод для проверки существования бакета
     * @param bucketName имя проверяемого бакета
     * @return true - сущесвует false - не существует
     */
    Boolean checkBucketExist(String bucketName) throws CannotCheckBucketInMinioException;
}
