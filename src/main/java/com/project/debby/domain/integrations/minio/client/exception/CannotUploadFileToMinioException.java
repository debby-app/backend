package com.project.debby.domain.integrations.minio.client.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotUploadFileToMinioException extends Exception {

    public CannotUploadFileToMinioException(String message) {
        super(String.format("Cannot upload file! Reason %s", message));
    }

}
