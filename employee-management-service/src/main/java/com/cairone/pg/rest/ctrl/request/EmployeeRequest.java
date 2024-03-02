package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.core.form.EmployeeForm;
import com.cairone.pg.base.enums.EmployeeTag;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
public class EmployeeRequest implements EmployeeForm {

    @NotBlank
    @Size(min = 1, max = 50)
    private String names;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private Long cityId;
    private Long bankAccountId;
    private Set<EmployeeTag> tags;

}
