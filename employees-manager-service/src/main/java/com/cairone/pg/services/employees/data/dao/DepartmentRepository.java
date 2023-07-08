package com.cairone.pg.services.employees.data.dao;

import com.cairone.pg.services.employees.data.domain.DepartmentEntity;
import com.cairone.pg.services.employees.data.AppDataRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepartmentRepository extends AppDataRepository<DepartmentEntity, Long> {
}
