package com.krazytop.teamfighttactics.exception;

import com.krazytop.teamfighttactics.model.generated.ApiErrorDTO;
import com.krazytop.teamfighttactics.model.generated.ApiErrorDTODetailsInner;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

import static com.krazytop.teamfighttactics.exception.ApiErrorEnum.*;
import static com.krazytop.teamfighttactics.exception.CustomApiError.buildApiResponseError;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNoController(NoHandlerFoundException ex) {
        log.warn(ex.getMessage());
        return buildApiResponseError(NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorDTO> handleNoController(HttpRequestMethodNotSupportedException ex) {
        log.warn(ex.getMessage());
        return buildApiResponseError(METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ApiErrorDTO> handleMethodArgumentTypeMismatch(MissingRequestHeaderException ex) {
        log.error("Missing header: {}", ex.getMessage(), ex);
        return buildApiResponseError(MISSING_HEADER, List.of(new ApiErrorDTODetailsInner(ex.getHeaderName(), "Missing")));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiErrorDTO> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Type mismatch: {}", ex.getMessage(), ex);
        return buildApiResponseError(TYPE_MISMATCHED);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiErrorDTO> handleCustomException(CustomException ex) {
        log.error("{}:", ex.getErrorEnum().getMessage(), ex);
        return buildApiResponseError(ex.getErrorEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiErrorDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("Validation failed: {}", ex.getMessage(), ex);
        List<ApiErrorDTODetailsInner> details = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            ApiErrorDTODetailsInner detail = new ApiErrorDTODetailsInner();
            detail.setField(error.getField());
            detail.setIssue(error.getDefaultMessage());
            details.add(detail);
        });
        return buildApiResponseError(VALIDATION_FAILED, details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation: {}", ex.getMessage(), ex);
        List<ApiErrorDTODetailsInner> details = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> {
            ApiErrorDTODetailsInner detail = new ApiErrorDTODetailsInner();
            String fieldName = violation.getPropertyPath() != null ? violation.getPropertyPath().toString() : "unknown";
            detail.setField(fieldName);
            detail.setIssue(violation.getMessage());
            details.add(detail);
        });
        return buildApiResponseError(CONSTRAINT_VIOLATION, details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleAllUncaughtException(Exception ex) {
        log.error("An unexpected server error occurred: {}", ex.getMessage(), ex);
        return buildApiResponseError(UNEXPECTED_RUNTIME_ERROR);
    }
}
