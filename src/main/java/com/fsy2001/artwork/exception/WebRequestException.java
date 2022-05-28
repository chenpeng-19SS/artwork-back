package com.fsy2001.artwork.exception;

import org.springframework.http.HttpStatus;

public class WebRequestException extends RuntimeException {
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public WebRequestException(String message) {
        super(message);
    }

    public WebRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
