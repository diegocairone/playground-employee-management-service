package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.core.form.BankForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class BankRequest implements BankForm {

    @NotBlank
    @Size(max = 30)
    private String name;

}
