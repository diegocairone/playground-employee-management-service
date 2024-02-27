package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.data.enums.EmployeeStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmployeeStatusRequest {

    @NotNull
    private EmployeeStatus newStatus;
}
