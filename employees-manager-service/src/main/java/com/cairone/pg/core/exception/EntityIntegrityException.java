package com.cairone.pg.core.exception;

public class EntityIntegrityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String field;

    public EntityIntegrityException() {
        super();
        this.field = null;
    }

    public EntityIntegrityException(String field, String format, Object... vars) {
        super(String.format(format, vars));
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
