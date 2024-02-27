package com.cairone.pg.rest.ctrl.validator;

import com.cairone.pg.base.enums.BankAccountType;
import com.cairone.pg.rest.ctrl.constraint.BankAccountNumberForTypeConstraint;
import com.cairone.pg.rest.ctrl.request.BankAccountRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BankAccountNumberForTypeValidator implements ConstraintValidator<BankAccountNumberForTypeConstraint, BankAccountRequest> {

    @Override
    public boolean isValid(BankAccountRequest value, ConstraintValidatorContext context) {
        String accountNumber = value.getAccountNumber().trim().toUpperCase();
        BankAccountType accountType = value.getAccountType();
        if (accountNumber.startsWith("SS") && !accountType.equals(BankAccountType.SAVINGS)) {
            addConstraintViolation(context, "Account number for SAVINGS account is invalid");
            return false;
        } else if (accountNumber.startsWith("CC") && !accountType.equals(BankAccountType.CHECKING)) {
            addConstraintViolation(context, "Account number for CHECKING account is invalid");
            return false;
        } else if (accountNumber.startsWith("OO") && !accountType.equals(BankAccountType.OTHER)) {
            addConstraintViolation(context, "Account number for OTHER account is invalid");
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("accountNumber")
                .addConstraintViolation();
    }
}
