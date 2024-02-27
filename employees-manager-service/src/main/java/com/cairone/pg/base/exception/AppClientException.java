package com.cairone.pg.base.exception;

public class AppClientException extends RuntimeException {

    public static final int UNKNOWN_CODE = 0;
    public static final int NOT_FOUND = 100;
    public static final int DATA_INTEGRITY = 200;

    private int code;

    public AppClientException(String message) {
        super(message);
        this.code = UNKNOWN_CODE;
    }

    public AppClientException(String message, Object... args) {
        this(String.format(message, args));
        this.code = UNKNOWN_CODE;
    }

    public AppClientException(int code, String message, Object... args) {
        super(String.format(message, args));
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
