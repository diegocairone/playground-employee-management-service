package com.cairone.pg.rest.endpoints;

import com.cairone.pg.core.mapper.BankAccountMapperCfg;
import com.cairone.pg.core.mapper.BankAccountFilter;
import com.cairone.pg.core.model.BankAccountModel;
import com.cairone.pg.rest.ctrl.request.BankAccountRequest;
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


@RequestMapping("/api/bank-accounts")
public interface BankAccountEndpoints {

    @Operation(summary = "Find bank account by ID")
    @GetMapping("{id}")
    ResponseEntity<BankAccountModel> findById(@PathVariable Long id, @ParameterObject BankAccountMapperCfg mapperCfg);

    @Operation(summary = "Find all bank accounts")
    @GetMapping
    ResponseEntity<Page<BankAccountModel>> findAll(
            @ParameterObject BankAccountFilter filter,
            @ParameterObject BankAccountMapperCfg mapperCfg,
            @ParameterObject @PageableDefault Pageable pageable);

    @Operation(summary = "Create a new bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bank account created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<BankAccountModel> create(@Valid @RequestBody BankAccountRequest request);

    @Operation(summary = "Update a bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank account updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("{id}")
    public ResponseEntity<BankAccountModel> update(@PathVariable Long id, @Valid @RequestBody BankAccountRequest request);

    @Operation(summary = "Delete a bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bank account deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id);
}
