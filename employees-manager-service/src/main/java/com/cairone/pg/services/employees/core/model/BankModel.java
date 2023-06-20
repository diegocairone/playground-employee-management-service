package com.cairone.pg.services.employees.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder(setterPrefix = "set")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BankModel {

    @EqualsAndHashCode.Include
    private final Long id;
    
    private final String name;
    
}
