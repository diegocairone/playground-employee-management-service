package com.cairone.pg.rest.ctrl;

import com.cairone.pg.base.exception.AppClientException;
import com.cairone.pg.base.vo.ErrorValue;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class AppAdviceCtrl {

    @ExceptionHandler(AppClientException.class)
    public ResponseEntity<ErrorValue> handleAppClientException(AppClientException ex) {
        String message = "Unknown error";
        if (ex.getCode() == AppClientException.NOT_FOUND) {
            message = "Requested resource not available";
        }
        if (ex.getCode() == AppClientException.DATA_INTEGRITY) {
            message = "Sent data is invalid";
        }
        ErrorValue appErrorResponse = ErrorValue.builder()
                .withMessage(message)
                .withReason(ex.getMessage())
                .build();

        ex.getErrors().forEach(appErrorResponse::addError);

        return ResponseEntity.badRequest().body(appErrorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorValue> handleValidationExceptions(MethodArgumentNotValidException ex) {

        ErrorValue.ErrorValueBuilder builder = ErrorValue.builder()
                .withMessage("At least one field in the request is invalid")
                .withReason("One or more fields are syntactically incorrect");

        BindingResult result = ex.getBindingResult();

        if (result.hasGlobalErrors()) {
            ObjectError objectError = Objects.requireNonNullElse(
                    result.getGlobalError(),
                    new ObjectError("global", "Unknown error"));
            builder.withReason(objectError.getDefaultMessage());
        }

        ErrorValue appErrorResponse = builder.build();

        result.getFieldErrors().forEach(fieldError -> appErrorResponse.addError(
            fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(appErrorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorValue> handleDataIntegrityViolationExceptions(DataIntegrityViolationException ex) {

        if (ex.getCause() instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) ex.getCause());
        }

        ErrorValue appErrorResponse = ErrorValue.builder()
                .withMessage("Data integrity violation")
                .withReason(
                        "Exception thrown when an attempt to insert, update or delete data results " +
                                "in violation of a business rule")
                .build();

        return ResponseEntity.badRequest().body(appErrorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorValue> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        ErrorValue appErrorResponse = ErrorValue.builder()
                .withMessage("Data syntactically incorrect")
                .withReason("One or more fields could not be read correctly")
                .build();

        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = ((InvalidFormatException) ex.getCause());
            appErrorResponse.addError(
                    invalidFormatException.getPath().get(0).getFieldName(),
                    "Invalid value provided");
        }

        return ResponseEntity.badRequest().body(appErrorResponse);
    }

    private ResponseEntity<ErrorValue> handleConstraintViolationException(ConstraintViolationException ex) {

        String constraintName = ex.getConstraintName();

        ErrorValue appErrorResponse = ErrorValue.builder()
                .withMessage("Data integrity violation")
                .withReason(
                        "Exception thrown when an attempt to insert, update or delete data results " +
                                "in violation of a business rule")
                .build();

        appErrorResponse.addError("constraintName", constraintName);

        return ResponseEntity.badRequest().body(appErrorResponse);
    }
}
