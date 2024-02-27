package com.cairone.pg.base.enums;

public enum BankAccountType {
    SAVINGS(1), CHECKING(2), OTHER(0);

    int dbValue;

    private BankAccountType(int dbValue) {
        this.dbValue = dbValue;
    }

    public int getDbValue() {
        return dbValue;
    }

    public static BankAccountType of(int dbValue) {
        switch (dbValue) {
            case 0:
                return OTHER;
            case 1:
                return SAVINGS;
            case 2:
                return CHECKING;
            default:
                throw new RuntimeException("Unknown value: " + dbValue);
        }
    }
}
