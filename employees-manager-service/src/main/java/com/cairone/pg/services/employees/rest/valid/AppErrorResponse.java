package com.cairone.pg.services.employees.rest.valid;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
@Builder(setterPrefix = "with")
public class AppErrorResponse {

    private final Map<String, List<String>> fieldErrors = new HashMap<>();
    private final String message;
    private final String reason;

    public void addError(String field, String errorMessage) {
        if (!fieldErrors.containsKey(field)) {
            fieldErrors.put(field, new ArrayList<>());
        }
        fieldErrors.get(field).add(errorMessage);
    }

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
}
