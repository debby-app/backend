package com.project.debby.domain.integrations.minio.client.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotCheckBucketInMinioException extends Exception{

    public CannotCheckBucketInMinioException(String message) {
        super(message);
    }
}
