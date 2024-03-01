package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.rest.ctrl.constraint.ManagerIsNotADepartmentEmployeeConstraint;
import com.cairone.pg.core.form.DepartmentForm;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Data
@ManagerIsNotADepartmentEmployeeConstraint
public class DepartmentRequest implements DepartmentForm {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    private Long managerId;

    private Set<Long> employeeIDs;

}
