package com.cairone.pg.services.employees.rest.ctrl;

import com.cairone.pg.services.employees.core.mapper.EmployeeFilter;
import com.cairone.pg.services.employees.core.mapper.EmployeeMapperCfg;
import com.cairone.pg.services.employees.core.model.BankAccountModel;
import com.cairone.pg.services.employees.core.model.EmployeeModel;
import com.cairone.pg.services.employees.core.service.BankAccountService;
import com.cairone.pg.services.employees.core.service.EmployeeService;
import com.cairone.pg.services.employees.rest.ctrl.request.EmployeeRequest;
import com.cairone.pg.services.employees.rest.ctrl.request.EmployeeStatusRequest;
import com.cairone.pg.services.employees.rest.exception.ResourceNotFoundException;
import com.cairone.pg.services.employees.rest.endpoints.EmployeeEndpoints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class EmployeeCtrl implements EmployeeEndpoints {

    private final EmployeeService employeeService;
    private final BankAccountService bankAccountService;


    @Override
    public ResponseEntity<EmployeeModel> findById(
            @PathVariable Long id,
            EmployeeMapperCfg mapperCfg) {
        
        EmployeeModel employee = employeeService.findById(id, mapperCfg)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resource with ID %s was not found", id));
        
        return ResponseEntity.ok(employee);
    }
    
    @Override
    public ResponseEntity<Page<EmployeeModel>> findAll(
            EmployeeFilter filter, EmployeeMapperCfg mapperCfg, Pageable pageable) {
        return ResponseEntity.ok(employeeService.findAll(filter, mapperCfg, pageable));
    }

    @Override
    public ResponseEntity<BankAccountModel> findBankAccountByEmployeeId(Long id) {
        return bankAccountService.findBankAccountByEmployeeId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resource with ID %s was not found", id));
    }

    @Override
    public ResponseEntity<EmployeeModel> create(EmployeeRequest request) {
        EmployeeModel employeeModel = employeeService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(employeeModel.getId()).toUri();
        return ResponseEntity.created(location).body(employeeModel);
    }

    @Override
    public ResponseEntity<EmployeeModel> update(Long id, EmployeeRequest request) {
        EmployeeModel employeeModel = employeeService.update(id, request);
        return ResponseEntity.ok(employeeModel);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateStatus(Long id, EmployeeStatusRequest request) {
        employeeService.updateStatus(id, request.getNewStatus());
        return ResponseEntity.noContent().build();
    }
}
