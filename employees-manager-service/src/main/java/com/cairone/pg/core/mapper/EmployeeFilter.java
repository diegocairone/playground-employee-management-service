package com.cairone.pg.core.mapper;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmployeeFilter {

    private final String startsWith;
}
