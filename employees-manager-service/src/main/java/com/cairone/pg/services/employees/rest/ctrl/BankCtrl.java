package com.cairone.pg.services.employees.rest.ctrl;

import java.net.URI;
import java.util.List;

import com.cairone.pg.services.employees.core.model.BankModel;
import com.cairone.pg.services.employees.core.service.BankService;
import com.cairone.pg.services.employees.rest.ctrl.request.BankRequest;
import com.cairone.pg.services.employees.rest.exception.ResourceNotFoundException;
import com.cairone.pg.services.employees.rest.endpoints.BankEndpoints;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BankCtrl implements BankEndpoints {

    private final BankService bankService;

    @Override
    public ResponseEntity<BankModel> findById(@PathVariable Long id) {
        
        return bankService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resource with ID %s was not found", id));
    }

    @Override
    public ResponseEntity<List<BankModel>> findAll() {
        return ResponseEntity.ok(bankService.findAll());
    }

    @Override
    public ResponseEntity<BankModel> create(BankRequest request) {
        BankModel bankModel = bankService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(bankModel.getId()).toUri();
        return ResponseEntity.created(location).body(bankModel);
    }

    @Override
    public ResponseEntity<BankModel> update(Long id, BankRequest request) {
        BankModel bankModel = bankService.update(id, request);
        return ResponseEntity.ok(bankModel);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        bankService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
