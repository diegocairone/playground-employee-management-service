
-- Sequence: employees_manager.banks_accounts

CREATE SEQUENCE IF NOT EXISTS employees_manager.bank_account_seq
    INCREMENT 1
    START 1
    MINVALUE 1;

ALTER SEQUENCE employees_manager.bank_account_seq
    OWNER TO postgres;


-- Sequence: employees_manager.banks

CREATE SEQUENCE IF NOT EXISTS employees_manager.bank_seq
    INCREMENT 1
    START 20
    MINVALUE 1;

ALTER SEQUENCE employees_manager.bank_seq
    OWNER TO postgres;


-- Sequence: employees_manager.cities

CREATE SEQUENCE IF NOT EXISTS employees_manager.city_seq
    INCREMENT 1
    START 20
    MINVALUE 1;

ALTER SEQUENCE employees_manager.city_seq
    OWNER TO postgres;


-- Sequence: employees_manager.departments

CREATE SEQUENCE IF NOT EXISTS employees_manager.department_seq
    INCREMENT 1
    START 1
    MINVALUE 1;

ALTER SEQUENCE employees_manager.department_seq
    OWNER TO postgres;


-- Sequence: employees_manager.employees

CREATE SEQUENCE IF NOT EXISTS employees_manager.employee_seq
    INCREMENT 1
    START 1
    MINVALUE 1;

ALTER SEQUENCE employees_manager.employee_seq
    OWNER TO postgres;
