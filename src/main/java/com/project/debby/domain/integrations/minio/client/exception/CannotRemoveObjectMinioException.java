package com.project.debby.domain.integrations.minio.client.exception;

public class CannotRemoveObjectMinioException extends Throwable{
    public CannotRemoveObjectMinioException(String message) {
        super(String.format("Cannot remove file! Reason %s", message));
    }
}
