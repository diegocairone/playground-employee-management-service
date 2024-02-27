package com.cairone.pg.rest.ctrl.validator;

import com.cairone.pg.rest.ctrl.constraint.ManagerIsNotADepartmentEmployeeConstraint;
import com.cairone.pg.rest.ctrl.request.DepartmentRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ManagerIsNotADepartmentEmployeeValidator implements ConstraintValidator<ManagerIsNotADepartmentEmployeeConstraint, DepartmentRequest> {
    @Override
    public boolean isValid(DepartmentRequest value, ConstraintValidatorContext context) {
        boolean isValid = value.getEmployeeIDs() == null || !value.getEmployeeIDs().contains(value.getManagerId());
        if (!isValid) {
            String message = String.format("Manager with ID %s cannot be included in the employee list", value.getManagerId());
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return isValid;
    }
}
