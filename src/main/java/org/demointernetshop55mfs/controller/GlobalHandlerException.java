package org.demointernetshop55mfs.controller;

import org.demointernetshop55mfs.service.exception.AlreadyExistException;
import org.demointernetshop55mfs.service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handlerNotFoundException(NotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handlerAlreadyExistException(AlreadyExistException e){
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error",e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handlerNullPointerException(NullPointerException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
