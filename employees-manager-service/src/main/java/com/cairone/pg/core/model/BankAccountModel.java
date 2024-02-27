package com.cairone.pg.core.model;

import com.cairone.pg.base.enums.BankAccountType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder(setterPrefix = "set")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BankAccountModel {

    @EqualsAndHashCode.Include
    private final Long id;

    private final BankModel bank;
    private final String accountNumber;
    private final BankAccountType accountType;
}
