package com.cairone.pg.data.converter;

import com.cairone.pg.base.enums.BankAccountType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BankAccountTypeConverter implements AttributeConverter<BankAccountType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BankAccountType attribute) {
        return attribute.getDbValue();
    }

    @Override
    public BankAccountType convertToEntityAttribute(Integer dbData) {
        return BankAccountType.of(dbData);
    }
}
