package com.dlut.ResearchService.component;

import com.dlut.ResearchService.exception.AccessException;
import com.dlut.ResearchService.exception.BusinessException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessException.class)
    public ResponseEntity<String> handleException(@NotNull Exception ex) {
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.FORBIDDEN;

        return new ResponseEntity<>(message, status);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleException(@NotNull BusinessException ex) {
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(message, status);
    }
}
