package com.cairone.pg.services.employees.data.dao;

import com.cairone.pg.services.employees.data.domain.EmployeeEntity;
import com.cairone.pg.services.employees.data.domain.EmployeeStatusLogEntity;
import com.cairone.pg.services.employees.data.AppDataRepository;
import com.cairone.pg.services.employees.data.domain.id.EmployeeStatusLogPkEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeStatusLogRepository extends AppDataRepository<EmployeeStatusLogEntity, EmployeeStatusLogPkEntity> {

    @Query("SELECT MAX(e.logId) FROM EmployeeStatusLogEntity e WHERE e.employee =?1")
    Optional<Long> getMaxId(EmployeeEntity employeeEntity);

}
