package com.cairone.pg.services.employees.core.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder(setterPrefix = "set")
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DepartmentModel {

    @EqualsAndHashCode.Include
    private final Long id;

    private final String name;
    private final EmployeeModel manager;
    private final Set<EmployeeModel> employees;
    
}
