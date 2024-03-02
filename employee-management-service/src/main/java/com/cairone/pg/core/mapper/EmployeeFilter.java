package com.cairone.pg.core.mapper;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class EmployeeFilter {

    private final String startsWith;
    private final UUID hasGlobalId;
}
