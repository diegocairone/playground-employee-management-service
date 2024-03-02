package com.cairone.pg.core.mapper;

import com.cairone.pg.data.domain.BankAccountEntity;
import com.cairone.pg.core.model.BankAccountModel;
import com.cairone.pg.core.model.BankModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankAccountMapper implements Converter<BankAccountEntity, BankAccountModel> {

    private final BankMapper bankMapper;

    @Override
    public BankAccountModel convert(BankAccountEntity source) {
        return BankAccountModel.builder()
                .setId(source.getId())
                .setBank(BankModel.builder()
                        .setId(source.getBank().getId())
                        .setName(source.getBank().getName())
                        .build())
                .setAccountNumber(source.getAccountNumber())
                .setAccountType(source.getAccountType())
                .build();
    }

    public BankAccountModel convert(BankAccountEntity source, BankAccountMapperCfg mapperCfg) {

        BankAccountModel.BankAccountModelBuilder builder = BankAccountModel.builder()
                .setId(source.getId())
                .setAccountNumber(source.getAccountNumber())
                .setAccountType(source.getAccountType());

        if (Boolean.TRUE.equals(mapperCfg.getIncludeBank())) {
            builder.setBank(bankMapper.convert(source.getBank()));
        }

        return builder.build();
    }
}
