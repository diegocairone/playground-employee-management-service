package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.core.form.CityForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CityRequest implements CityForm {

    @NotBlank
    @Size(max = 50)
    private String name;
}
