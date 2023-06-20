package com.cairone.pg.services.employees.core.service;

import java.util.Optional;

import com.cairone.pg.services.employees.core.exception.EntityNotFoundException;
import com.cairone.pg.services.employees.data.dao.BankAccountRepository;
import com.cairone.pg.services.employees.data.dao.BankRepository;
import com.cairone.pg.services.employees.data.dao.EmployeeRepository;
import com.cairone.pg.services.employees.data.domain.BankAccountEntity;
import com.cairone.pg.services.employees.data.domain.BankEntity;
import com.cairone.pg.services.employees.data.domain.QBankAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cairone.pg.services.employees.core.form.BankAccountForm;
import com.cairone.pg.services.employees.core.mapper.BankAccountFilter;
import com.cairone.pg.services.employees.core.mapper.BankAccountMapper;
import com.cairone.pg.services.employees.core.mapper.BankAccountMapperCfg;
import com.cairone.pg.services.employees.core.model.BankAccountModel;
import com.querydsl.core.BooleanBuilder;

import lombok.RequiredArgsConstructor;

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
        
        Long bankAccountId = bankAccountRepository.getMaxId().orElse(0L);
        BankEntity bankEntity = bankRepository.findById(form.getBankId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Requested resource could not be created",
                        "Bank with ID %s does not exist in our database", form.getBankId()));
        
        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        
        bankAccountEntity.setId(bankAccountId + 1L);
        bankAccountEntity.setAccountNumber(form.getAccountNumber().trim().toUpperCase());
        bankAccountEntity.setAccountType(form.getAccountType());
        bankAccountEntity.setBank(bankEntity);
        
        return bankAccountMapper.convert(bankAccountRepository.save(bankAccountEntity));
    }

    public BankAccountModel update(Long id, BankAccountForm form) {

        BankAccountEntity bankAccountEntity = bankAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Requested resource to be updated does not exist in the database",
                        "Bank account with ID %s could not be updated", id));

        bankAccountEntity.setAccountNumber(form.getAccountNumber().trim().toUpperCase());
        bankAccountEntity.setAccountType(form.getAccountType());

        if (!bankAccountEntity.getBank().getId().equals(form.getBankId())) {

            BankEntity bankEntity = bankRepository.findById(form.getBankId())
                  .orElseThrow(() -> new EntityNotFoundException(
                            "Requested resource could not be updated",
                            "Bank with ID %s does not exist in our database", form.getBankId()));

            bankAccountEntity.setBank(bankEntity);
        }

        return bankAccountMapper.convert(bankAccountRepository.save(bankAccountEntity));
    }

    public void delete(Long id) {

        BankAccountEntity bankAccountEntity = bankAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Requested resource to be deleted does not exist in the database",
                        "Bank account with ID %s could not be deleted", id));

        bankAccountRepository.delete(bankAccountEntity);
    }
}
