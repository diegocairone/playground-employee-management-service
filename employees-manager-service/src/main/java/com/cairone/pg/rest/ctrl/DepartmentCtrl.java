package com.cairone.pg.rest.ctrl;

import com.cairone.pg.base.exception.AppClientException;
import com.cairone.pg.core.mapper.DepartmentMapperCfg;
import com.cairone.pg.core.model.DepartmentModel;
import com.cairone.pg.core.service.DepartmentService;
import com.cairone.pg.rest.ctrl.request.DepartmentRequest;
import com.cairone.pg.rest.endpoints.DepartmentEndpoints;
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
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("id", "Invalid ID provided"),
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
