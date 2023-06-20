package com.cairone.pg.services.employees.core.form;

import com.cairone.pg.services.employees.data.enums.BankAccountType;

public interface BankAccountForm {

    public String getAccountNumber();
    public BankAccountType getAccountType();
    public Long getBankId();
    
}
