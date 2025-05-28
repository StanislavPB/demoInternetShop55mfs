package org.demointernetshop55mfs.security.service;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String message) {
        super(message);
    }
}
