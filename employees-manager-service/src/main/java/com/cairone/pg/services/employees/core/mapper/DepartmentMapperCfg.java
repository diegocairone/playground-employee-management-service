package com.cairone.pg.services.employees.core.mapper;

import lombok.Data;

@Data
public class DepartmentMapperCfg {

    private Boolean includeManager;
    private Boolean includeEmployees;
    private EmployeeMapperCfg employee;
    
}
