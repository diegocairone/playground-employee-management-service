package com.cairone.pg.services.employees.core.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.cairone.pg.services.employees.data.domain.DepartmentEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.cairone.pg.services.employees.core.model.DepartmentModel;
import com.cairone.pg.services.employees.core.model.EmployeeModel;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DepartmentMapper implements Converter<DepartmentEntity, DepartmentModel> {

    private final EmployeeMapper employeeMapper;
    
    @Override
    public DepartmentModel convert(DepartmentEntity source) {
        return DepartmentModel.builder()
                .setId(source.getId())
                .setName(source.getName())
                .build();
    }
    
    public DepartmentModel convert(DepartmentEntity source, DepartmentMapperCfg mapperCfg) {
        
        DepartmentModel.DepartmentModelBuilder builder = DepartmentModel.builder();
        
        builder.setId(source.getId());
        builder.setName(source.getName());

        if (Boolean.TRUE.equals(mapperCfg.getIncludeEmployees())) {
            Set<EmployeeModel> employeeModels = source.getEmployees()
                    .stream()
                    .map(entity -> employeeMapper.convert(entity, mapperCfg.getEmployee()))
                    .collect(Collectors.toSet());
            builder.setEmployees(employeeModels);
        }
        
        if (Boolean.TRUE.equals(mapperCfg.getIncludeManager())) {
            EmployeeModel managerModel = employeeMapper.convert(
                    source.getManager(), mapperCfg.getEmployee());
            builder.setManager(managerModel);
        }
        
        return builder.build();
    }
}
