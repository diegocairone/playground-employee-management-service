package com.cairone.pg.services.employees.rest.endpoints;

import com.cairone.pg.services.employees.core.model.CityModel;
import com.cairone.pg.services.employees.rest.ctrl.request.CityRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/cities")
public interface CityEndpoints {

    @Operation(summary = "Find city by ID")
    @GetMapping("{id}")
    public ResponseEntity<CityModel> findById(@PathVariable Long id);

    @Operation(summary = "Find all cities")
    @GetMapping
    public ResponseEntity<Page<CityModel>> findAll(
            @ParameterObject @PageableDefault(size = 10) Pageable pageable);

    @Operation(summary = "Create a new city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "City created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CityModel> create(@Valid @RequestBody CityRequest request);

    @Operation(summary = "Update a city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("{id}")
    public ResponseEntity<CityModel> update(Long id, @Valid @RequestBody CityRequest request);

    @Operation(summary = "Delete a city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "City deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(Long id);
}
