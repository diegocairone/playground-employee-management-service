package com.cairone.pg.services.employees.rest.ctrl.constraint;

import com.cairone.pg.services.employees.rest.ctrl.validator.ManagerIsNotADepartmentEmployeeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ManagerIsNotADepartmentEmployeeValidator.class)
public @interface ManagerIsNotADepartmentEmployeeConstraint {
    String message() default "Manager must not be a department employee";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
