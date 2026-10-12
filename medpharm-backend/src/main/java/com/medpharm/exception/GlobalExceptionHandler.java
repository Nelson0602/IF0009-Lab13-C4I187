package com.medpharm.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Manejo centralizado de errores usando el formato RFC 7807 (ProblemDetail)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return buildProblem(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException ex) {
        return buildProblem(HttpStatus.CONFLICT, "Regla de negocio incumplida", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        return buildProblem(HttpStatus.BAD_REQUEST, "Solicitud inválida", ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        return buildProblem(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadableBody(HttpMessageNotReadableException ex) {
        return buildProblem(HttpStatus.BAD_REQUEST, "Cuerpo inválido",
                "El cuerpo de la solicitud no tiene un formato JSON válido");
    }

    // Reúne los errores de validación campo por campo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        ProblemDetail problem = buildProblem(HttpStatus.BAD_REQUEST, "Datos inválidos",
                "Hay campos que no cumplen las validaciones");
        problem.setProperty("errors", fieldErrors);
        return problem;
    }

    private ProblemDetail buildProblem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setProperty("timestamp", LocalDateTime.now().toString());
        return problem;
    }
}