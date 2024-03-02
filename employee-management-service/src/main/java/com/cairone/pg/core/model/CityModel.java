package com.cairone.pg.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder(setterPrefix = "set")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CityModel {

    @EqualsAndHashCode.Include
    private final Long id;
    
    private final String name;
}
