package com.cairone.pg.rest.ctrl.request;

import com.cairone.pg.core.form.CityForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CityRequest implements CityForm {

    @NotBlank
    @Size(max = 50)
    private String name;
}
