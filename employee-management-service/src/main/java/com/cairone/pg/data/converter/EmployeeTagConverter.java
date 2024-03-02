package com.cairone.pg.data.converter;


import com.cairone.pg.base.enums.EmployeeTag;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeeTagConverter implements AttributeConverter<EmployeeTag, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeTag attribute) {
        return attribute.getDbValue();
    }

    @Override
    public EmployeeTag convertToEntityAttribute(Integer dbData) {
        return EmployeeTag.of(dbData);
    }
}
