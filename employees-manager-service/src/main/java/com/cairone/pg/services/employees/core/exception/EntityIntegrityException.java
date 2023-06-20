package com.cairone.pg.services.employees.core.exception;

public class EntityIntegrityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String field;

    public EntityIntegrityException() {
        super();
    }

    public EntityIntegrityException(String field, String format, Object... vars) {
        super(String.format(format, vars));
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
