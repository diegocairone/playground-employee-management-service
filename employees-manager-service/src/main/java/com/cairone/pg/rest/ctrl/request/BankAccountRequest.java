package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.base.enums.BankAccountType;
import com.cairone.pg.core.form.BankAccountForm;
import com.cairone.pg.rest.ctrl.constraint.BankAccountNumberForTypeConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
