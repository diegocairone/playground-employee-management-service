package com.cairone.pg.services.employees.rest.ctrl.request;

import com.cairone.pg.services.employees.core.form.CityForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CityRequest implements CityForm {

    @NotBlank
    private String name;
}
