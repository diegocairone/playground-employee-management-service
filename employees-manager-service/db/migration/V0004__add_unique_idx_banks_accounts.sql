ALTER TABLE IF EXISTS employees_manager.banks_accounts
    ADD CONSTRAINT banks_accounts_number_key UNIQUE (account_number);
