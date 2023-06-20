package com.cairone.pg.services.employees.rest.ctrl;

import com.cairone.pg.services.employees.core.mapper.DepartmentMapperCfg;
import com.cairone.pg.services.employees.core.model.DepartmentModel;
import com.cairone.pg.services.employees.core.service.DepartmentService;
import com.cairone.pg.services.employees.rest.ctrl.request.DepartmentRequest;
import com.cairone.pg.services.employees.rest.exception.ResourceNotFoundException;
import com.cairone.pg.services.employees.rest.endpoints.DepartmentEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DepartmentCtrl implements DepartmentEndpoints {

    private final DepartmentService departmentService;

    @Override
    public ResponseEntity<DepartmentModel> findById(
            Long id,
            DepartmentMapperCfg departmentMapperCfg) {
        
        return departmentService.findById(id, departmentMapperCfg)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resource with ID %s was not found", id));
    }

    @Override
    public ResponseEntity<List<DepartmentModel>> findAll(DepartmentMapperCfg mapperCfg) {
        return ResponseEntity.ok(departmentService.findAll(mapperCfg));
    }

    @Override
    public ResponseEntity<DepartmentModel> create(DepartmentRequest request) {
        DepartmentModel departmentModel = departmentService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(departmentModel.getId()).toUri();
        return ResponseEntity.created(location).body(departmentModel);
    }

    @Override
    public ResponseEntity<DepartmentModel> update(Long id, DepartmentRequest request) {
        DepartmentModel departmentModel = departmentService.update(id, request);
        return ResponseEntity.ok(departmentModel);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
