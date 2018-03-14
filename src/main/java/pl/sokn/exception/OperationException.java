package pl.sokn.exception;

import org.springframework.http.HttpStatus;

public class OperationException extends Exception {
    private static final long serialVersionUID = -7257797438269434346L;
    private final HttpStatus status;
    OperationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
