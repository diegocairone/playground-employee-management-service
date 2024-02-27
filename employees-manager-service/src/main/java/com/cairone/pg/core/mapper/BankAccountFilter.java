package com.cairone.pg.core.mapper;

import java.util.Set;

import com.cairone.pg.base.enums.BankAccountType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BankAccountFilter {

    private final String accountNumber;
    private final AccountIs accountIs;
    private final Set<BankAccountType> typeIn;

    public enum AccountIs {
        EQUAL_TO, STARTS_WITH, ENDS_WITH, CONTAINS;
    }
}
