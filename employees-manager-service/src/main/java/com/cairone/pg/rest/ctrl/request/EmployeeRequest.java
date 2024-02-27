package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.core.form.EmployeeForm;
import com.cairone.pg.data.enums.EmployeeTag;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
