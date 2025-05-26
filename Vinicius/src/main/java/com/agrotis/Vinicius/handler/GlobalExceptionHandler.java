package com.agrotis.Vinicius.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.agrotis.Vinicius.exception.DataInvalidaException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataInvalidaException.class)
    public ResponseEntity<String> handleDataInvalida(DataInvalidaException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
