package com.cairone.pg.services.employees.rest.ctrl.request;

import com.cairone.pg.services.employees.data.enums.EmployeeStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmployeeStatusRequest {

    @NotNull
    private EmployeeStatus newStatus;
}
