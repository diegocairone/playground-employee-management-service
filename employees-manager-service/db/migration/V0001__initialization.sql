-- Database: EMPLOYEES_MANAGER_SERVICE

-- Table: employees_manager.banks

-- DROP TABLE IF EXISTS employees_manager.banks;

CREATE TABLE IF NOT EXISTS employees_manager.banks
(
    bank_id integer NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT banks_pkey PRIMARY KEY (bank_id),
    CONSTRAINT banks_name_key UNIQUE (name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS employees_manager.banks
    OWNER to postgres;

-- Table: employees_manager.banks_accounts

-- DROP TABLE IF EXISTS employees_manager.banks_accounts;

CREATE TABLE IF NOT EXISTS employees_manager.banks_accounts
(
    account_id integer NOT NULL,
    account_number character varying(10) COLLATE pg_catalog."default" NOT NULL,
    account_type integer NOT NULL,
    bank_id integer NOT NULL,
    CONSTRAINT banks_accounts_pkey PRIMARY KEY (account_id),
    CONSTRAINT banks_accounts_bank_id_fkey FOREIGN KEY (bank_id)
        REFERENCES employees_manager.banks (bank_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS employees_manager.banks_accounts
    OWNER to postgres;
	

-- Table: employees_manager.cities

-- DROP TABLE IF EXISTS employees_manager.cities;

CREATE TABLE IF NOT EXISTS employees_manager.cities
(
    city_id integer NOT NULL,
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT cities_pkey PRIMARY KEY (city_id),
    CONSTRAINT cities_name_key UNIQUE (name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS employees_manager.cities
    OWNER to postgres;

-- Table: employees_manager.employees

-- DROP TABLE IF EXISTS employees_manager.employees;

CREATE TABLE IF NOT EXISTS employees_manager.employees
(
    employee_id integer NOT NULL,
    names character varying(50) COLLATE pg_catalog."default" NOT NULL,
    birthdate date NOT NULL,
    city_id integer NOT NULL,
    status integer NOT NULL,
    bank_account_id integer,
    CONSTRAINT employees_pkey PRIMARY KEY (employee_id),
    CONSTRAINT employees_bank_account_id_fkey FOREIGN KEY (bank_account_id)
        REFERENCES employees_manager.banks_accounts (account_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT employees_city_id_fkey FOREIGN KEY (city_id)
        REFERENCES employees_manager.cities (city_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS employees_manager.employees
    OWNER to postgres;

-- Table: employees_manager.employees_status_log

-- DROP TABLE IF EXISTS employees_manager.employees_status_log;

CREATE TABLE IF NOT EXISTS employees_manager.employees_status_log
(
    employee_id integer NOT NULL,
    log_id integer NOT NULL,
    log_datetime timestamp without time zone NOT NULL,
    previous_status integer NOT NULL,
    current_status integer NOT NULL,
    CONSTRAINT employees_status_log_pkey PRIMARY KEY (employee_id, log_id),
    CONSTRAINT employees_status_log_employee_id_fkey FOREIGN KEY (employee_id)
        REFERENCES employees_manager.employees (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS employees_manager.employees_status_log
    OWNER to postgres;


-- Table: employees_manager.employees_tags

-- DROP TABLE IF EXISTS employees_manager.employees_tags;

CREATE TABLE IF NOT EXISTS employees_manager.employees_tags
(
    employee_id integer NOT NULL,
    tag_id integer NOT NULL,
    CONSTRAINT employees_tags_pkey PRIMARY KEY (employee_id, tag_id),
    CONSTRAINT employees_tags_employee_id_fkey FOREIGN KEY (employee_id)
        REFERENCES employees_manager.employees (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS employees_manager.employees_tags
    OWNER to postgres;

-- Table: employees_manager.departments

-- DROP TABLE IF EXISTS employees_manager.departments;

CREATE TABLE IF NOT EXISTS employees_manager.departments
(
    department_id integer NOT NULL,
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    manager_id integer NOT NULL,
    CONSTRAINT departments_pkey PRIMARY KEY (department_id),
    CONSTRAINT departments_manager_id_fkey FOREIGN KEY (manager_id)
        REFERENCES employees_manager.employees (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS employees_manager.departments
    OWNER to postgres;

-- Table: employees_manager.departments_employees

-- DROP TABLE IF EXISTS employees_manager.departments_employees;

CREATE TABLE IF NOT EXISTS employees_manager.departments_employees
(
    department_id integer NOT NULL,
    employee_id integer NOT NULL,
    CONSTRAINT departments_employees_pkey PRIMARY KEY (department_id, employee_id),
    CONSTRAINT departments_employees_department_id_fkey FOREIGN KEY (department_id)
        REFERENCES employees_manager.departments (department_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT departments_employees_employee_id_fkey FOREIGN KEY (employee_id)
        REFERENCES employees_manager.employees (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS employees_manager.departments_employees
    OWNER to postgres;


