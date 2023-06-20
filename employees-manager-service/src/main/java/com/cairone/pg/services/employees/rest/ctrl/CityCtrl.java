package com.cairone.pg.services.employees.rest.ctrl;

import com.cairone.pg.services.employees.core.model.CityModel;
import com.cairone.pg.services.employees.core.service.CityService;
import com.cairone.pg.services.employees.rest.ctrl.request.CityRequest;
import com.cairone.pg.services.employees.rest.exception.ResourceNotFoundException;
import com.cairone.pg.services.employees.rest.endpoints.CityEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CityCtrl implements CityEndpoints {

    private final CityService cityService;
    
    @Override
    public ResponseEntity<CityModel> findById(@PathVariable Long id) {
        
        return cityService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resource with ID %s was not found", id));
    }

    @Override
    public ResponseEntity<Page<CityModel>> findAll(Pageable pageable) {
        return ResponseEntity.ok(cityService.findAll(pageable));
    }

    @Override
    public ResponseEntity<CityModel> create(CityRequest request) {
        CityModel cityModel = cityService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(cityModel.getId()).toUri();
        return ResponseEntity.created(location).body(cityModel);
    }

    @Override
    public ResponseEntity<CityModel> update(Long id, CityRequest request) {
        CityModel cityModel = cityService.update(id, request);
        return ResponseEntity.ok(cityModel);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        cityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
