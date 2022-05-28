package com.fsy2001.artwork.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(value = {WebRequestException.class})
    public ResponseEntity<Object> handleWebRequestException(WebRequestException e) {
        WebException webException = new WebException(
                e.getMessage(), e.getHttpStatus(), ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(webException, e.getHttpStatus());
    }
}
