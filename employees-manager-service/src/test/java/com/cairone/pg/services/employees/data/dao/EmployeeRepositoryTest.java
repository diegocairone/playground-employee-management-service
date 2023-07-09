package com.cairone.pg.services.employees.data.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;

import com.cairone.pg.services.employees.data.domain.DepartmentEntity;
import com.cairone.pg.services.employees.data.domain.EmployeeEntity;
import com.cairone.pg.services.employees.data.enums.EmployeeTag;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Test
    public void givenEmployeeId_whenFindById_thenOk() {
        Optional<EmployeeEntity> employee = employeeRepository.findById(1L);
        assertThat(employee).isNotEmpty();
    }
    
    @Test
    public void givenEmployee_thenAssertHasDepartments() {
        
        Optional<EmployeeEntity> employeeOptional = employeeRepository.findById(2L);
        assertThat(employeeOptional).isNotEmpty();
        
        EmployeeEntity employeeEntity = employeeOptional.get();
        Set<DepartmentEntity> departmentEntities = employeeEntity.getDepartments();
        assertThat(departmentEntities).hasSize(1);
    }
    
    @Test
    public void givenEmployee_thenAssertHasTags() {
        
        Optional<EmployeeEntity> employeeOptional = employeeRepository.findById(2L);
        assertThat(employeeOptional).isNotEmpty();
        
        EmployeeEntity employeeEntity = employeeOptional.get();
        Assertions.assertThat(employeeEntity.getTags()).hasSize(2);
        
        Assertions.assertThat(employeeEntity.getTags()).contains(EmployeeTag.BACKEND_DEV);
        Assertions.assertThat(employeeEntity.getTags()).contains(EmployeeTag.JAVA_DEV);
    }
}
