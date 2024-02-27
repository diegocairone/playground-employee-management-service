package com.cairone.pg.core.exception;

@Deprecated
public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String reason;

    public EntityNotFoundException() {
        super();
        this.reason = null;
    }

    public EntityNotFoundException(String reason, String format, Object... vars) {
        super(String.format(format, vars));
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
