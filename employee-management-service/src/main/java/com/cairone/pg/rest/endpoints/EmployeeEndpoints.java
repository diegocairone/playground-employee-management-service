package com.cairone.pg.rest.endpoints;

import com.cairone.pg.base.enums.EmployeeTag;
import com.cairone.pg.core.mapper.EmployeeFilter;
import com.cairone.pg.core.mapper.EmployeeMapperCfg;
import com.cairone.pg.core.model.BankAccountModel;
import com.cairone.pg.core.model.EmployeeModel;
import com.cairone.pg.rest.ctrl.request.EmployeeRequest;
import com.cairone.pg.rest.ctrl.request.EmployeeStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@RequestMapping("/api/employees")
public interface EmployeeEndpoints {

    @Operation(summary = "Find employee by ID")
    @GetMapping("{id}")
    ResponseEntity<EmployeeModel> findById(
            @PathVariable Long id, @ParameterObject EmployeeMapperCfg mapperCfg);

    @Operation(summary = "Find bank account by employee ID")
    @GetMapping("{id}/bank-account")
    ResponseEntity<BankAccountModel> findBankAccountByEmployeeId(
            @PathVariable Long id);

    @Operation(summary = "Find all employees")
    @GetMapping
    ResponseEntity<Page<EmployeeModel>> findAll(
            @ParameterObject EmployeeFilter filter,
            @ParameterObject EmployeeMapperCfg mapperCfg,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable);

    @Operation(summary = "Create a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    ResponseEntity<EmployeeModel> create(@Valid @RequestBody EmployeeRequest request);

    @Operation(summary = "Update an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("{id}")
    ResponseEntity<EmployeeModel> update(
            @PathVariable Long id, @Valid @RequestBody EmployeeRequest request);

    @Operation(summary = "Delete an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @DeleteMapping("{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @Operation(summary = "Update an employee status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee status updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("{id}/status")
    ResponseEntity<Void> updateStatus(
            @PathVariable Long id, @Valid @RequestBody EmployeeStatusRequest request);

    @Operation(summary = "Get all employee tags")
    @GetMapping("/tags")
    ResponseEntity<List<EmployeeTag>> getAllEmployeeTag();
}
