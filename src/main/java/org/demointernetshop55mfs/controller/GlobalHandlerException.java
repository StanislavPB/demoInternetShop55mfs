package org.demointernetshop55mfs.controller;

import jakarta.validation.ConstraintViolationException;
import org.demointernetshop55mfs.dto.ApiError;
import org.demointernetshop55mfs.service.exception.AlreadyExistException;
import org.demointernetshop55mfs.service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {
        ApiError error = ApiError.builder()
                .error("Not Found")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ApiError> handleAlreadyExistException(AlreadyExistException e) {
        ApiError error = ApiError.builder()
                .error("Conflict")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException e) {
        List<Map<String, Object>> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    Map<String, Object> details = new HashMap<>();
                    details.put("field", error.getField());
                    details.put("message", error.getDefaultMessage());
                    details.put("rejectedValue", error.getRejectedValue());
                    return details;
                })
                .toList();

        ApiError error = ApiError.builder()
                .error("Validation Failed")
                .message("One or more fields are invalid")
                .errors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        ApiError error = ApiError.builder()
                .error("Invalid parameter")
                .message("Failed to convert parameter")
                .parameter(e.getName())
                .rejectedValue(e.getValue())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .reduce("", (s1, s2) -> s1 + s2 + "; ");

        ApiError error = ApiError.builder()
                .error("Constraint Violation")
                .message(msg)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception e) {
        ApiError error = ApiError.builder()
                .error("Internal Server Error")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
