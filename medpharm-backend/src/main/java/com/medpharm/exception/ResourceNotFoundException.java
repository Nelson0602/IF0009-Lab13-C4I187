package com.medpharm.exception;

// Se lanza cuando un registro no existe en la base de datos
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}