package com.cairone.pg.core.mapper;

import com.cairone.pg.data.domain.CityEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.cairone.pg.core.model.CityModel;

@Component
public class CityMapper implements Converter<CityEntity, CityModel> {

    @Override
    public CityModel convert(CityEntity source) {
        return CityModel.builder()
                .setId(source.getId())
                .setName(source.getName())
                .build();
    }
}
