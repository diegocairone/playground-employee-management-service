package com.cairone.pg.services.employees.rest.ctrl.request;

import com.cairone.pg.services.employees.core.form.BankForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class BankRequest implements BankForm {

    @NotBlank
    @Size(min = 1, max = 30)
    private String name;

}
