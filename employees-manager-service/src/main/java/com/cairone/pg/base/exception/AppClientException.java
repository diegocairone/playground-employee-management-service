package com.cairone.pg.base.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AppClientException extends RuntimeException {

    public static final int UNKNOWN_CODE = 0;
    public static final int NOT_FOUND = 100;
    public static final int DATA_INTEGRITY = 200;

    private final int code;
    private final Map<String, String> errors = new HashMap<>();

    public AppClientException(String message) {
        super(message);
        this.code = UNKNOWN_CODE;
    }

    public AppClientException(String message, Object... args) {
        this(String.format(message, args));
    }

    public AppClientException(int code, String message, Object... args) {
        super(String.format(message, args));
        this.code = code;
    }

    public AppClientException(int code, Consumer<Map<String, String>> consumer, String message, Object... args) {
        super(String.format(message, args));
        consumer.accept(errors);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public AppClientException addError(String field, String message, Object... args) {
        errors.put(field, String.format(message, args));
        return this;
    }
}
