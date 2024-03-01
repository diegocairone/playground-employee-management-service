package com.cairone.pg.data.converter;


import com.cairone.pg.base.enums.EmployeeStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeeStatusConverter implements AttributeConverter<EmployeeStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeStatus attribute) {
        return attribute.getDbValue();
    }

    @Override
    public EmployeeStatus convertToEntityAttribute(Integer dbData) {
        return EmployeeStatus.of(dbData);
    }
}
