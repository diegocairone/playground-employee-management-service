package com.cairone.pg.data.enums;

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
            throw new RuntimeException("Unknown dbValue: " + dbValue);
        }
    }
}
