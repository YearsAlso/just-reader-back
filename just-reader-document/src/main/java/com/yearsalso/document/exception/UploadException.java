package com.yearsalso.document.exception;

/**
 * 上传异常
 */
public class UploadException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public UploadException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public UploadException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // 预定义的错误码
    public static final String FILE_TOO_LARGE = "FILE_TOO_LARGE";
    public static final String UNSUPPORTED_FILE_TYPE = "UNSUPPORTED_FILE_TYPE";
    public static final String UPLOAD_FAILED = "UPLOAD_FAILED";
    public static final String CHUNK_UPLOAD_FAILED = "CHUNK_UPLOAD_FAILED";
    public static final String UPLOAD_NOT_FOUND = "UPLOAD_NOT_FOUND";
    public static final String UPLOAD_EXPIRED = "UPLOAD_EXPIRED";
    public static final String PARSE_FAILED = "PARSE_FAILED";
    public static final String STORAGE_ERROR = "STORAGE_ERROR";
    public static final String VALIDATION_FAILED = "VALIDATION_FAILED";
}