package com.cairone.pg.rest.ctrl.constraint;

import com.cairone.pg.rest.ctrl.validator.ManagerIsNotADepartmentEmployeeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ManagerIsNotADepartmentEmployeeValidator.class)
public @interface ManagerIsNotADepartmentEmployeeConstraint {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
