package com.cairone.pg.core.form;

import com.cairone.pg.data.enums.BankAccountType;

public interface BankAccountForm {

    public String getAccountNumber();
    public BankAccountType getAccountType();
    public Long getBankId();
    
}
