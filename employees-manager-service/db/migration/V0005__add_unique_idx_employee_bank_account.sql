ALTER TABLE IF EXISTS employees_manager.employees
    ADD CONSTRAINT employee_bank_account_id_unique UNIQUE (bank_account_id);
