package com.hobbyFinder.hubby.exception;

import com.hobbyFinder.hubby.exception.AuthException.Login.CredenciaisLoginException;
import com.hobbyFinder.hubby.exception.AuthException.Registro.CredenciaisRegistroException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private CustomErrorType defaultCustomErrorType(String message){
        return CustomErrorType.builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .errors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(CredenciaisLoginException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorType> handleCredenciaisLoginException(CredenciaisLoginException ex) {
        return ResponseEntity.status(ex.getStatus()).body(defaultCustomErrorType(ex.getMessage()));
    }

    @ExceptionHandler(CredenciaisRegistroException.class)
    @ResponseBody
    public  ResponseEntity<CustomErrorType> handleCredenciaisRegistroException(CredenciaisRegistroException ex) {
        return ResponseEntity.status(ex.getStatus()).body(defaultCustomErrorType(ex.getMessage()));
    }

    @ExceptionHandler(HubbyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CustomErrorType handleHubbyException(HubbyException ex) {
        return defaultCustomErrorType(ex.getMessage());
    }
}
