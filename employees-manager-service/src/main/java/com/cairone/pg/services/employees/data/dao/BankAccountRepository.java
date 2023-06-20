package com.cairone.pg.services.employees.data.dao;

import java.util.Optional;

import com.cairone.pg.services.employees.data.domain.BankAccountEntity;
import org.springframework.data.jpa.repository.Query;

import com.cairone.pg.services.employees.data.AppDataRepository;

public interface BankAccountRepository extends AppDataRepository<BankAccountEntity, Long> {

    @Query("SELECT max(c.id) FROM BankAccountEntity c")
    public Optional<Long> getMaxId();
}
