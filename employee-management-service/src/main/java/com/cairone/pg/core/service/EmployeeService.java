package com.cairone.pg.core.service;

import com.cairone.pg.base.enums.EmployeeStatus;
import com.cairone.pg.base.exception.AppClientException;
import com.cairone.pg.core.form.EmployeeForm;
import com.cairone.pg.core.mapper.EmployeeFilter;
import com.cairone.pg.core.mapper.EmployeeMapper;
import com.cairone.pg.core.mapper.EmployeeMapperCfg;
import com.cairone.pg.core.model.EmployeeModel;
import com.cairone.pg.core.pageable.EmployeePageableConverter;
import com.cairone.pg.data.dao.BankAccountRepository;
import com.cairone.pg.data.dao.CityRepository;
import com.cairone.pg.data.dao.EmployeeRepository;
import com.cairone.pg.data.dao.EmployeeStatusLogRepository;
import com.cairone.pg.data.domain.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeStatusLogRepository employeeStatusLogRepository;
    private final CityRepository cityRepository;
    private final BankAccountRepository bankAccountRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeePageableConverter employeePageableConverter;

    @Transactional(readOnly = true)
    public Optional<EmployeeModel> findById(Long id, EmployeeMapperCfg mapperCfg) {
        return employeeRepository.findById(id)
                .map(entity -> employeeMapper.convert(entity, mapperCfg));
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeModel> findByGlobalId(UUID globalId, EmployeeMapperCfg mapperCfg) {
        QEmployeeEntity query = QEmployeeEntity.employeeEntity;
        Predicate predicate = query.globalId.eq(globalId);
        return employeeRepository.findOne(predicate)
                .map(entity -> employeeMapper.convert(entity, mapperCfg));
    }

    @Transactional(readOnly = true)
    public Page<EmployeeModel> findAll(EmployeeFilter filter, EmployeeMapperCfg mapperCfg, Pageable viewPageable) {

        QEmployeeEntity query = QEmployeeEntity.employeeEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (filter != null) {
            if (filter.getStartsWith() != null && !filter.getStartsWith().isBlank()) {
                builder.and(query.names.startsWithIgnoreCase(filter.getStartsWith()));
            }
            if (filter.getHasGlobalId() != null) {
                builder.and(query.globalId.eq(filter.getHasGlobalId()));
            }
        }

        Pageable dataPageable = employeePageableConverter.convert(viewPageable);
        Assert.notNull(dataPageable, "Data pageable cannot be null");

        return employeeRepository.findAll(builder, dataPageable)
                .map(entity -> employeeMapper.convert(entity, mapperCfg));
    }

    @Transactional
    public EmployeeModel create(EmployeeForm form) {

        final CityEntity cityEntity = cityRepository.findById(form.getCityId())
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("cityId", "Invalid City ID provided"),
                        "City with ID %s could not be found in the database",
                        form.getCityId()));

        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setGlobalId(UUID.randomUUID());
        employeeEntity.setNames(form.getNames().trim().toUpperCase());
        employeeEntity.setBirthDate(form.getBirthDate());
        employeeEntity.setCity(cityEntity);
        employeeEntity.setStatus(EmployeeStatus.INACTIVE);
        employeeEntity.setTags(form.getTags());

        if (form.getBankAccountId() != null) {
            BankAccountEntity bankAccountEntity = bankAccountRepository.findById(form.getBankAccountId())
                    .orElseThrow(() -> new AppClientException(
                            AppClientException.NOT_FOUND,
                            error -> error.put("bankAccountId", "Invalid Bank Account ID provided"),
                            "Bank Account with ID %s could not be found in the database",
                            form.getBankAccountId()));
            employeeEntity.setBankAccount(bankAccountEntity);
        }

        return employeeMapper.convert(employeeRepository.save(employeeEntity));
    }

    @Transactional
    public EmployeeModel update(Long id, EmployeeForm form) {

        final EmployeeEntity employeeEntity = employeeRepository.findById(id).orElseThrow(
                () -> getEmployeeNotFoundException(id));

        if (!employeeEntity.getCity().getId().equals(form.getCityId())) {

            final CityEntity cityEntity = cityRepository.findById(form.getCityId())
                    .orElseThrow(() -> new AppClientException(
                            AppClientException.NOT_FOUND,
                            error -> error.put("cityId", "Invalid City ID provided"),
                            "City with ID %s could not be found in the database",
                            form.getCityId()));

            employeeEntity.setCity(cityEntity);
        }

        if (form.getBankAccountId() == null) {
            employeeEntity.setBankAccount(null);
        } else if (!employeeEntity.getBankAccount().getId().equals(form.getBankAccountId())) {
            final BankAccountEntity bankAccountEntity = bankAccountRepository.findById(form.getBankAccountId())
                    .orElseThrow(() -> new AppClientException(
                            AppClientException.NOT_FOUND,
                            error -> error.put("bankAccountId", "Invalid Bank Account ID provided"),
                            "Bank Account with ID %s could not be found in the database",
                            form.getBankAccountId()));
            employeeEntity.setBankAccount(bankAccountEntity);
        }

        employeeEntity.setNames(form.getNames().trim().toUpperCase());
        employeeEntity.setBirthDate(form.getBirthDate());
        employeeEntity.setTags(form.getTags());

        return employeeMapper.convert(employeeRepository.save(employeeEntity));
    }

    @Transactional
    public void delete(Long id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id)
                .orElseThrow(() -> getEmployeeNotFoundException(id));
        employeeRepository.delete(employeeEntity);
    }

    @Transactional
    public void updateStatus(Long employeeId, EmployeeStatus newStatus) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> getEmployeeNotFoundException(employeeId));
        EmployeeStatus currentStatus = employeeEntity.getStatus();
        if (!currentStatus.equals(newStatus)) {
            Long nextLogId = employeeStatusLogRepository.getMaxId(employeeEntity).orElse(0L) + 1L;
            EmployeeStatusLogEntity statusLogEntity = new EmployeeStatusLogEntity(employeeEntity, nextLogId);
            statusLogEntity.setDateTime(LocalDateTime.now());
            statusLogEntity.setPreviousStatus(currentStatus);
            statusLogEntity.setStatus(newStatus);
            employeeStatusLogRepository.save(statusLogEntity);
        }
    }

    private AppClientException getEmployeeNotFoundException(Long id) {
        return new AppClientException(
                AppClientException.NOT_FOUND,
                error -> error.put("id", "Invalid ID provided"),
                "Employee with ID %s could not be found in the database", id);
    }
}
