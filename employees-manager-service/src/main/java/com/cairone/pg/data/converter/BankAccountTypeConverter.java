package com.cairone.pg.data.converter;

import com.cairone.pg.base.enums.BankAccountType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


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
