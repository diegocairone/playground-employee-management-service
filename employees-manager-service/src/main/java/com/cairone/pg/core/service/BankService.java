package com.cairone.pg.core.service;

import com.cairone.pg.base.exception.AppClientException;
import com.cairone.pg.core.form.BankForm;
import com.cairone.pg.core.mapper.BankMapper;
import com.cairone.pg.core.model.BankModel;
import com.cairone.pg.data.dao.BankRepository;
import com.cairone.pg.data.domain.BankEntity;
import com.cairone.pg.data.domain.QBankEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    @Transactional(readOnly = true)
    public Optional<BankModel> findById(Long id) {
        return bankRepository.findById(id).map(bankMapper::convert);
    }

    @Transactional(readOnly = true)
    public List<BankModel> findAll() {
        return bankRepository.findAll()
                .stream()
                .map(bankMapper::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    public BankModel create(BankForm form) {

        // check business rules
        String name = form.getName().trim().toUpperCase();
        verifyDuplicatedName(name, q -> q.name.eq(name));

        BankEntity bankEntity = new BankEntity();
        bankEntity.setName(form.getName().trim().toUpperCase());
        return bankMapper.convert(bankRepository.save(bankEntity));
    }

    @Transactional
    public BankModel update(Long id, BankForm form) {

        BankEntity bankEntity = bankRepository.findById(id)
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("id", "Invalid ID provided"),
                        "Bank with ID %s could not be updated", id));

        // check business rules
        String name = form.getName().trim().toUpperCase();
        verifyDuplicatedName(name, q -> q.name.eq(name).and(q.id.ne(id)));

        bankEntity.setName(form.getName().trim().toUpperCase());
        return bankMapper.convert(bankRepository.save(bankEntity));
    }

    @Transactional
    public void delete(Long id) {
        BankEntity bankEntity = bankRepository.findById(id)
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("id", "Invalid ID provided"),
                        "Bank with ID %s could not be deleted", id));
        bankRepository.delete(bankEntity);
    }

    private void verifyDuplicatedName(String name, Function<QBankEntity, BooleanExpression> predicate) {
        boolean existsByName = exists(predicate);
        if (existsByName) {
            throw new AppClientException(
                    AppClientException.DATA_INTEGRITY,
                    error -> error.put("name", "Provided bank name is already in use"),
                    "Bank with name %s already exists", name);
        }
    }

    private Boolean exists(Function<QBankEntity, BooleanExpression> predicate) {
        QBankEntity qBankEntity = QBankEntity.bankEntity;
        BooleanExpression booleanExpression = predicate.apply(qBankEntity);
        return bankRepository.exists(booleanExpression);
    }
}
