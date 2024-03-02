package com.cairone.pg.core.form;

import com.cairone.pg.base.enums.BankAccountType;

public interface BankAccountForm {

    public String getAccountNumber();
    public BankAccountType getAccountType();
    public Long getBankId();
    
}
