package com.cairone.pg.services.employees.data.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;

import com.cairone.pg.services.employees.data.domain.DepartmentEntity;
import com.cairone.pg.services.employees.data.domain.EmployeeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Test
    void givenDepartment_thenAssertHasEmployees() {
        
        Optional<DepartmentEntity> departmentOptional = departmentRepository.findById(1L);
        assertThat(departmentOptional).isNotEmpty();
        
        DepartmentEntity departmentEntity = departmentOptional.get();
        Set<EmployeeEntity> employeeEntities = departmentEntity.getEmployees();
        assertThat(employeeEntities).hasSize(2);
    }
}
