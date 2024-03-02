package com.cairone.pg.core.service;

import com.cairone.pg.base.exception.AppClientException;
import com.cairone.pg.core.form.BankAccountForm;
import com.cairone.pg.core.mapper.BankAccountFilter;
import com.cairone.pg.core.mapper.BankAccountMapper;
import com.cairone.pg.core.mapper.BankAccountMapperCfg;
import com.cairone.pg.core.model.BankAccountModel;
import com.cairone.pg.data.dao.BankAccountRepository;
import com.cairone.pg.data.dao.BankRepository;
import com.cairone.pg.data.dao.EmployeeRepository;
import com.cairone.pg.data.domain.BankAccountEntity;
import com.cairone.pg.data.domain.BankEntity;
import com.cairone.pg.data.domain.QBankAccountEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final EmployeeRepository employeeRepository;
    private final BankRepository bankRepository;
    private final BankAccountRepository bankAccountRepository;

    private final BankAccountMapper bankAccountMapper;

    public Optional<BankAccountModel> findById(Long id, BankAccountMapperCfg mapperCfg) {
        return bankAccountRepository.findById(id).map(e -> bankAccountMapper.convert(e, mapperCfg));
    }

    public Optional<BankAccountModel> findBankAccountByEmployeeId(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employee -> bankAccountMapper.convert(employee.getBankAccount()));
    }

    public Page<BankAccountModel> findAll(BankAccountFilter filter, BankAccountMapperCfg mapperCfg, Pageable viewPageable) {

        QBankAccountEntity query = QBankAccountEntity.bankAccountEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (filter != null) {
            
            if (filter.getAccountNumber() != null && !filter.getAccountNumber().isBlank()) {
    
                if (filter.getAccountIs().equals(BankAccountFilter.AccountIs.EQUAL_TO)) {
                    builder.and(query.accountNumber.eq(filter.getAccountNumber()));
                } else if (filter.getAccountIs().equals(BankAccountFilter.AccountIs.STARTS_WITH)) {
                    builder.and(query.accountNumber.startsWithIgnoreCase(filter.getAccountNumber()));
                } else if (filter.getAccountIs().equals(BankAccountFilter.AccountIs.ENDS_WITH)) {
                    builder.and(query.accountNumber.endsWithIgnoreCase(filter.getAccountNumber()));
                } else if (filter.getAccountIs().equals(BankAccountFilter.AccountIs.CONTAINS)) {
                    builder.and(query.accountNumber.containsIgnoreCase(filter.getAccountNumber()));
                }
            }
            
            if (filter.getTypeIn() != null && !filter.getTypeIn().isEmpty()) {
                builder.and(query.accountType.in(filter.getTypeIn()));
            }
        }

        return bankAccountRepository.findAll(builder, viewPageable)
                .map(e -> bankAccountMapper.convert(e, mapperCfg));
    }
    
    @Transactional
    public BankAccountModel create(BankAccountForm form) {

        BankEntity bankEntity = bankRepository.findById(form.getBankId())
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("bankId", "Invalid Bank ID provided"),
                        "Bank with ID %s does not exist in our database", form.getBankId()));

        // check business rules
        String accountNumber = form.getAccountNumber().trim().toUpperCase();
        verifyDuplicatedName(accountNumber, q -> q.accountNumber.eq(accountNumber));

        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        
        bankAccountEntity.setAccountNumber(form.getAccountNumber().trim().toUpperCase());
        bankAccountEntity.setAccountType(form.getAccountType());
        bankAccountEntity.setBank(bankEntity);
        
        return bankAccountMapper.convert(bankAccountRepository.save(bankAccountEntity));
    }

    public BankAccountModel update(Long id, BankAccountForm form) {

        BankAccountEntity bankAccountEntity = bankAccountRepository.findById(id)
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("bankAccountId", "Invalid Bank Account ID provided"),
                        "Bank account with ID %s could not be updated", id));

        // check business rules
        String accountNumber = form.getAccountNumber().trim().toUpperCase();
        verifyDuplicatedName(accountNumber, q -> q.accountNumber.eq(accountNumber).and(q.id.ne(id)));

        bankAccountEntity.setAccountNumber(form.getAccountNumber().trim().toUpperCase());
        bankAccountEntity.setAccountType(form.getAccountType());

        if (!bankAccountEntity.getBank().getId().equals(form.getBankId())) {

            BankEntity bankEntity = bankRepository.findById(form.getBankId())
                  .orElseThrow(() -> new AppClientException(
                          AppClientException.NOT_FOUND,
                          error -> error.put("bankId", "Invalid Bank ID provided"),
                          "Bank with ID %s does not exist in our database", form.getBankId()));

            bankAccountEntity.setBank(bankEntity);
        }

        return bankAccountMapper.convert(bankAccountRepository.save(bankAccountEntity));
    }

    public void delete(Long id) {

        BankAccountEntity bankAccountEntity = bankAccountRepository.findById(id)
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("bankAccountId", "Invalid Bank Account ID provided"),
                        "Bank account with ID %s could not be deleted", id));

        bankAccountRepository.delete(bankAccountEntity);
    }

    private void verifyDuplicatedName(String bankAccountNumber, Function<QBankAccountEntity, BooleanExpression> predicate) {
        boolean existsByName = exists(predicate);
        if (existsByName) {
            throw new AppClientException(
                    AppClientException.DATA_INTEGRITY,
                    error -> error.put("accountNumber", "Provided account number is already in use"),
                    "Bank account number %s already exists",
                    bankAccountNumber);
        }
    }

    private Boolean exists(Function<QBankAccountEntity, BooleanExpression> predicate) {
        QBankAccountEntity qBankAccountEntity = QBankAccountEntity.bankAccountEntity;
        BooleanExpression booleanExpression = predicate.apply(qBankAccountEntity);
        return bankAccountRepository.exists(booleanExpression);
    }
}
