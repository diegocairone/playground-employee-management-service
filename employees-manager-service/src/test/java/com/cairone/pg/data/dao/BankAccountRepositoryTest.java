package com.cairone.pg.data.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.cairone.pg.data.domain.BankAccountEntity;
import com.cairone.pg.data.domain.EmployeeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class BankAccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;
    
    @Test
    void test() {
        
        Optional<BankAccountEntity> bankAccountOptional = bankAccountRepository.findById(1L);
        assertThat(bankAccountOptional).isNotEmpty();
        
        BankAccountEntity bankAccountEntity = bankAccountOptional.get();
        EmployeeEntity employeeEntity = bankAccountEntity.getEmployee();
        
        assertThat(employeeEntity.getId()).isEqualTo(1L);
    }
}
