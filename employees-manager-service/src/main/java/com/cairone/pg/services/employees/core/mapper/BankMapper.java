package com.cairone.pg.services.employees.core.mapper;

import com.cairone.pg.services.employees.data.domain.BankEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.cairone.pg.services.employees.core.model.BankModel;

@Component
public class BankMapper implements Converter<BankEntity, BankModel> {

    @Override
    public BankModel convert(BankEntity source) {
        return BankModel.builder()
                .setId(source.getId())
                .setName(source.getName())
                .build();
    }
}
