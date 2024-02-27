package com.cairone.pg.base.enums;

public enum EmployeeTag {
    BACKEND_DEV(1),
    FRONTEND_DEV(2),
    FULLSTACK_DEV(3),
    JAVA_DEV(4),
    REACT_DEV(5);

    int dbValue;
    
    private EmployeeTag(int dbValue) {
        this.dbValue = dbValue;
    }

    public int getDbValue() {
        return dbValue;
    }
    
    public static EmployeeTag of(int dbValue) {
        switch (dbValue) {
        case 1:
            return BACKEND_DEV;
        case 2:
            return FRONTEND_DEV;
        case 3:
            return FULLSTACK_DEV;
        case 4:
            return JAVA_DEV;
        case 5:
            return REACT_DEV;
        default:
            throw new RuntimeException("Unknown dbValue: " + dbValue);
        }
    }
}
