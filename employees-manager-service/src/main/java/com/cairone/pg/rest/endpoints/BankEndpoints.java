package com.cairone.pg.rest.endpoints;

import java.util.List;

import javax.validation.Valid;

import com.cairone.pg.rest.ctrl.request.BankRequest;
import com.cairone.pg.core.model.BankModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequestMapping("/api/banks")
public interface BankEndpoints {

    @Operation(summary = "Find bank by ID")
    @GetMapping("{id}")
    ResponseEntity<BankModel> findById(@PathVariable Long id);

    @Operation(summary = "Find all banks")
    @GetMapping
    ResponseEntity<List<BankModel>> findAll();

    @Operation(summary = "Create a new bank")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bank created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<BankModel> create(@Valid @RequestBody BankRequest request);

    @Operation(summary = "Update a bank")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("{id}")
    public ResponseEntity<BankModel> update(Long id, @Valid @RequestBody BankRequest request);

    @Operation(summary = "Delete a bank")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bank deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(Long id);
}
