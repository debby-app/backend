package com.project.debby.domain.integrations.minio.client.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotCreateBucketInMinioException extends Exception{

    public CannotCreateBucketInMinioException(String message) {
        super(String.format("Cannot create bucket! Reason %s", message));
    }

}
