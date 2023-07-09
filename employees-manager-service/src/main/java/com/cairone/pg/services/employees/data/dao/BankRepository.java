package com.cairone.pg.services.employees.data.dao;

import com.cairone.pg.services.employees.data.domain.BankEntity;
import com.cairone.pg.services.employees.data.AppDataRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BankRepository extends AppDataRepository<BankEntity, Long> {
}
