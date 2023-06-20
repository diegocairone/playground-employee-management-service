package com.cairone.pg.services.employees.rest.ctrl.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.cairone.pg.services.employees.core.form.BankAccountForm;
import com.cairone.pg.services.employees.data.enums.BankAccountType;

import lombok.Data;

@Data
public class BankAccountRequest implements BankAccountForm {

    @NotBlank
    private String accountNumber;
    
    @NotNull
    private BankAccountType accountType;
    
    @NotNull
    private Long bankId;
    
}
