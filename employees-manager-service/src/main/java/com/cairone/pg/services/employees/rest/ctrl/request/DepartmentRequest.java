package com.cairone.pg.services.employees.rest.ctrl.request;

import com.cairone.pg.services.employees.core.form.DepartmentForm;
import com.cairone.pg.services.employees.rest.ctrl.constraint.ManagerIsNotADepartmentEmployeeConstraint;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
