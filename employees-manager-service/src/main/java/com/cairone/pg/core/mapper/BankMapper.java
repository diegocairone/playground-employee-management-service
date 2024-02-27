package com.cairone.pg.core.mapper;

import com.cairone.pg.core.model.BankModel;
import com.cairone.pg.data.domain.BankEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

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
