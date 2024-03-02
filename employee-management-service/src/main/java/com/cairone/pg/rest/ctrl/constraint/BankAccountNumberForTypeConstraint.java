package com.cairone.pg.rest.ctrl.constraint;

import com.cairone.pg.rest.ctrl.validator.BankAccountNumberForTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BankAccountNumberForTypeValidator.class)
public @interface BankAccountNumberForTypeConstraint {

    String message() default "Provided account number is invalid for this account type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
