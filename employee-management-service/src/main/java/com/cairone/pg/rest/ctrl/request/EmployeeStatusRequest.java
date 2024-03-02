package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.base.enums.EmployeeStatus;
import lombok.Data;

import jakarta.validation.constraints.NotNull;


@Data
public class EmployeeStatusRequest {

    @NotNull
    private EmployeeStatus newStatus;
}
