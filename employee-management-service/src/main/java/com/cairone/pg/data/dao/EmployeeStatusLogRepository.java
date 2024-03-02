package com.cairone.pg.data.dao;

import com.cairone.pg.data.AppDataRepository;
import com.cairone.pg.data.domain.EmployeeStatusLogEntity;
import com.cairone.pg.data.domain.EmployeeEntity;
import com.cairone.pg.data.domain.id.EmployeeStatusLogPkEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeStatusLogRepository extends AppDataRepository<EmployeeStatusLogEntity, EmployeeStatusLogPkEntity> {

    @Query("SELECT MAX(e.logId) FROM EmployeeStatusLogEntity e WHERE e.employee =?1")
    Optional<Long> getMaxId(EmployeeEntity employeeEntity);

}
