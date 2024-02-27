package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.core.form.BankForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class BankRequest implements BankForm {

    @NotBlank
    @Size(max = 30)
    private String name;

}
