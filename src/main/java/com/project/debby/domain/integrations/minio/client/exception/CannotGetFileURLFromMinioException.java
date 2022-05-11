package com.project.debby.domain.integrations.minio.client.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotGetFileURLFromMinioException extends Exception{

    public CannotGetFileURLFromMinioException(String message) {
        super(String.format("Cannot get file url! Reason %s", message));
    }

}
