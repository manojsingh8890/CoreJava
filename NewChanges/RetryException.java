package com.ibm.sec.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This is custom exception, solely meant for business logic validation errors. For any technical errors regular Exception types must be used.
 */
@Getter
public class RetryException extends RuntimeException {

	private String message;
    private String httpStatus;
    private String code;
    private String sessionId;

    public RetryException(String httpStatus, String code, String message) {
        init(httpStatus, code, message);
    }

    public RetryException(String httpStatus, String code, String message, Throwable cause) {
        super(cause);
        init(httpStatus, code, message);
    }

    private void init(String httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
