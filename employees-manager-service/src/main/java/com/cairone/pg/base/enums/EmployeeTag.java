package com.cairone.pg.base.enums;

public enum EmployeeTag {
    BACKEND_DEV(1),
    FRONTEND_DEV(2),
    FULLSTACK_DEV(3),
    DEVOPS(4),
    QA(5),
    PRODUCT_MANAGER(6),
    PROJECT_MANAGER(7),
    SCRUM_MASTER(8);

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
            return DEVOPS;
        case 5:
            return QA;
        case 6:
            return PRODUCT_MANAGER;
        case 7:
            return PROJECT_MANAGER;
        case 8:
            return SCRUM_MASTER;
        default:
            throw new RuntimeException("Unknown dbValue: " + dbValue);
        }
    }
}
