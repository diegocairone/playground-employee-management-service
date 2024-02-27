package com.cairone.pg.rest.ctrl.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.cairone.pg.core.form.BankAccountForm;
import com.cairone.pg.data.enums.BankAccountType;

import com.cairone.pg.rest.ctrl.constraint.BankAccountNumberForTypeConstraint;
import lombok.Data;

@Data
@BankAccountNumberForTypeConstraint
public class BankAccountRequest implements BankAccountForm {

    @NotBlank
    private String accountNumber;
    
    @NotNull
    private BankAccountType accountType;
    
    @NotNull
    private Long bankId;
    
}
