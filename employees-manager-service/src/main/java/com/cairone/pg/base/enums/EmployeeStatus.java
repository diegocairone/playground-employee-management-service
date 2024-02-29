package com.cairone.pg.base.enums;

import com.cairone.pg.base.exception.AppClientException;

public enum EmployeeStatus {
    ACTIVE(1),
    INACTIVE(2),
    SUSPENDED(3)
    ;
    
    int dbValue;
    
    private EmployeeStatus(int dbValue) {
        this.dbValue = dbValue;
    }

    public int getDbValue() {
        return dbValue;
    }
    
    public static EmployeeStatus of(int dbValue) {
        switch (dbValue) {
        case 1:
            return ACTIVE;
        case 2:
            return INACTIVE;
        case 3:
            return SUSPENDED;
        default:
            throw new AppClientException(
                    AppClientException.DATA_INTEGRITY,
                    errors -> errors.put("status", "Invalid employee status type: " + dbValue),
                    "Invalid employee status type: " + dbValue);
        }
    }
}
