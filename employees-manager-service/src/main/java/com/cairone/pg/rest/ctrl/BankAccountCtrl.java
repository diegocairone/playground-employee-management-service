package com.cairone.pg.rest.ctrl;

import java.net.URI;

import com.cairone.pg.core.mapper.BankAccountFilter;
import com.cairone.pg.core.mapper.BankAccountMapperCfg;
import com.cairone.pg.core.service.BankAccountService;
import com.cairone.pg.rest.ctrl.request.BankAccountRequest;
import com.cairone.pg.rest.endpoints.BankAccountEndpoints;
import com.cairone.pg.rest.exception.ResourceNotFoundException;
import com.cairone.pg.core.model.BankAccountModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BankAccountCtrl implements BankAccountEndpoints {

    private final BankAccountService bankAccountService;

    @Override
    public ResponseEntity<BankAccountModel> findById(Long id, BankAccountMapperCfg mapperCfg) {
        return bankAccountService.findById(id, mapperCfg)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resource with ID %s was not found", id));
    }

    @Override
    public ResponseEntity<Page<BankAccountModel>> findAll(
            BankAccountFilter filter, BankAccountMapperCfg mapperCfg, Pageable pageable) {
        return ResponseEntity.ok(bankAccountService.findAll(filter, mapperCfg, pageable));
    }

    @Override
    public ResponseEntity<BankAccountModel> create(BankAccountRequest request) {
        BankAccountModel bankAccountModel = bankAccountService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(bankAccountModel.getId()).toUri();
        return ResponseEntity.created(location).body(bankAccountModel);
    }

    @Override
    public ResponseEntity<BankAccountModel> update(Long id, BankAccountRequest request) {
        BankAccountModel bankAccountModel = bankAccountService.update(id, request);
        return ResponseEntity.ok(bankAccountModel);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        bankAccountService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
