package com.cairone.pg.rest.endpoints;

import com.cairone.pg.core.mapper.DepartmentMapperCfg;
import com.cairone.pg.core.model.DepartmentModel;
import com.cairone.pg.rest.ctrl.request.DepartmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RequestMapping("/api/departments")
public interface DepartmentEndpoints {

    @Operation(summary = "Find department by ID")
    @GetMapping("{id}")
    ResponseEntity<DepartmentModel> findById(
            @PathVariable("id") Long id, @ParameterObject DepartmentMapperCfg departmentMapperCfg);

    @Operation(summary = "Find all departments")
    @GetMapping
    ResponseEntity<List<DepartmentModel>> findAll(@ParameterObject DepartmentMapperCfg mapperCfg);

    @Operation(summary = "Create a new department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Department created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<DepartmentModel> create(@Valid @RequestBody DepartmentRequest request);

    @Operation(summary = "Update a department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("{id}")
    public ResponseEntity<DepartmentModel> update(@PathVariable("id") Long id, @Valid @RequestBody DepartmentRequest request);

    @Operation(summary = "Delete a department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Department deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id);
}
