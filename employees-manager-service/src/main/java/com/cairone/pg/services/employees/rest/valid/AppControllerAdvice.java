package com.cairone.pg.services.employees.rest.valid;

import com.cairone.pg.services.employees.core.exception.EntityIntegrityException;
import com.cairone.pg.services.employees.core.exception.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleEntityNotFoundExceptions(EntityNotFoundException ex) {
        AppErrorResponse appErrorResponse = AppErrorResponse.builder()
                .withMessage(ex.getMessage())
                .withReason(ex.getReason())
                .build();

        return ResponseEntity.badRequest().body(appErrorResponse);
    }

    @ExceptionHandler(EntityIntegrityException.class)
    public ResponseEntity<AppErrorResponse> handleEntityIntegrityExceptions(EntityIntegrityException ex) {
        AppErrorResponse appErrorResponse = AppErrorResponse.builder()
                .withMessage("Data integrity violation")
                .withReason(
                        "Exception thrown when an attempt to insert or update data results " +
                                "in violation of a business rule")
                .build();
        appErrorResponse.addError(ex.getField(), ex.getMessage());
        return ResponseEntity.badRequest().body(appErrorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        AppErrorResponse.AppErrorResponseBuilder builder = AppErrorResponse.builder()
                .withMessage("At least one field in the request is invalid")
                .withReason("One or more fields are syntactically incorrect");

        BindingResult result = ex.getBindingResult();

        if (result.hasGlobalErrors()) {
            builder.withReason(result.getGlobalError().getDefaultMessage());
        }

        AppErrorResponse appErrorResponse = builder.build();

        result.getFieldErrors().forEach(fieldError -> appErrorResponse.addError(
            fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(appErrorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<AppErrorResponse> handleDataIntegrityViolationExceptions(DataIntegrityViolationException ex) {

        AppErrorResponse appErrorResponse = AppErrorResponse.builder()
                .withMessage("Data integrity violation")
                .withReason(
                        "Exception thrown when an attempt to insert, update or delete data results " +
                                "in violation of a business rule")
                .build();

        return ResponseEntity.badRequest().body(appErrorResponse);
    }
}
