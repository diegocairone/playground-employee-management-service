package com.cairone.pg.rest.ctrl;

import com.cairone.pg.base.exception.AppClientException;
import com.cairone.pg.core.model.BankModel;
import com.cairone.pg.core.service.BankService;
import com.cairone.pg.rest.ctrl.request.BankRequest;
import com.cairone.pg.rest.endpoints.BankEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BankCtrl implements BankEndpoints {

    private final BankService bankService;

    @Override
    public ResponseEntity<BankModel> findById(@PathVariable Long id) {
        
        return bankService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new AppClientException(
                        AppClientException.NOT_FOUND,
                        error -> error.put("id", "Invalid ID provided"),
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
    public ResponseEntity<BankModel> update(@PathVariable Long id, BankRequest request) {
        BankModel bankModel = bankService.update(id, request);
        return ResponseEntity.ok(bankModel);
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bankService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
