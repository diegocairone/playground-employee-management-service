package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.rest.ctrl.constraint.ManagerIsNotADepartmentEmployeeConstraint;
import com.cairone.pg.core.form.DepartmentForm;
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
