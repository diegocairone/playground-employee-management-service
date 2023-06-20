package com.cairone.pg.services.employees.core.mapper;

import com.cairone.pg.services.employees.data.domain.EmployeeEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.cairone.pg.services.employees.core.model.CityModel;
import com.cairone.pg.services.employees.core.model.EmployeeModel;
import com.cairone.pg.services.employees.core.model.EmployeeModel.EmployeeModelBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeMapper implements Converter<EmployeeEntity, EmployeeModel> {

    private final CityMapper cityMapper;
    
    @Override
    public EmployeeModel convert(EmployeeEntity source) {
        CityModel city = cityMapper.convert(source.getCity());
        EmployeeModel employeeModel = EmployeeModelBuilder.builder()
                .setId(source.getId())
                .setNames(source.getNames())
                .setBirthDate(source.getBirthDate())
                .setCity(city)
                .build();
        return employeeModel;
    }
    
    public EmployeeModel convert(EmployeeEntity source, EmployeeMapperCfg mapperCfg) {
        EmployeeModel.EmployeeModelBuilder builder = EmployeeModelBuilder.builder();
        if (mapperCfg != null && Boolean.TRUE.equals(mapperCfg.getIncludeCity())) {
            CityModel city = cityMapper.convert(source.getCity());
            builder.setCity(city);
        }
        builder.setId(source.getId())
                .setNames(source.getNames())
                .setBirthDate(source.getBirthDate());        
        return builder.build();
    }
}
