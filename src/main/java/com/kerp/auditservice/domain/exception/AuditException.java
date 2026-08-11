package com.kerp.auditservice.domain.exception;

import org.springframework.http.HttpStatus;

public class AuditException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public AuditException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
    public AuditException(HttpStatus status, String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }


    public static AuditException notFound(String resource, String field, String id) {
        return new AuditException(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                resource + " avec "+field+" : " + id+ " est introuvable !"
        );
    }

    public static AuditException alreadyExists(String resource, String field, String value) {
        return new AuditException(
                HttpStatus.CONFLICT,
                "ALREADY_EXISTS",
                resource + " Dèjà existant avec " + field + ": " + value
        );
    }

    public static AuditException badRequest(String message) {
        return new AuditException(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                message
        );
    }

    public static AuditException fileTooLarge(String message) {
        return new AuditException(
                HttpStatus.BAD_REQUEST,
                "FILE_TOO_LARGE",
                message
        );
    }

    public static AuditException forbidden(String message) {
        return new AuditException(
                HttpStatus.FORBIDDEN,
                "FORBIDDEN",
                message
        );
    }

    public static AuditException internalError(String message) {
        return new AuditException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                message
        );
    }
    public static AuditException BusinessException(String message,String field) {
        return new AuditException(
                HttpStatus.CONFLICT,
                field,
                message
        );
    }
}
