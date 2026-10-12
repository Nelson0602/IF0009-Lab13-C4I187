package com.medpharm.exception;

// Se lanza cuando se incumple una regla del negocio, por ejemplo stock insuficiente
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}