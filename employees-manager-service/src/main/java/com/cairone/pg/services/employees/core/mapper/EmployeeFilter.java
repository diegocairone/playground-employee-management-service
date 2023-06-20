package com.cairone.pg.services.employees.core.mapper;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmployeeFilter {

    private final String startsWith;
}
