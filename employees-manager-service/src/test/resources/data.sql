INSERT INTO cities (city_id, name) VALUES
(1, 'BUENOS AIRES'),
(2, 'ROSARIO'),
(3, 'SPRINGFIELD'),
(4, 'NEW YORK'),
(5, 'LONDON'),
(6, 'PARIS'),
(7, 'MADRID'),
(8, 'MOSCOW'),
(9, 'BEIJING'),
(10, 'TOKYO'),
(11, 'SYDNEY'),
(12, 'SANTIAGO'),
(13, 'MONTEVIDEO'),
(14, 'SAO PAULO'),
(15, 'LIMA')
;

INSERT INTO banks (bank_id, name) VALUES
(1, 'BANK 1'),
(2, 'BANK 2')
;

INSERT INTO banks_accounts (account_id, account_number, account_type, bank_id) VALUES
(1, 'SS00000001', 1, 1),
(2, 'SS00000002', 1, 1),
(3, 'OO00000003', 0, 2)
;

INSERT INTO employees
(employee_id, names, birthdate, city_id, status, bank_account_id)
VALUES
(1, 'EMPLOYEE-1', '1980-02-03', 1, 1, 1),
(2, 'EMPLOYEE-2', '1980-02-03', 1, 1, 2),
(3, 'EMPLOYEE-3', '1980-02-03', 1, 1, 3),
(4, 'HOMER SIMPSON', '1980-02-03', 3, 1, null),
(5, 'MARGE SIMPSON', '1980-02-03', 3, 1, null),
(6, 'BART SIMPSON', '1990-02-03', 3, 1, null),
(7, 'LISA SIMPSON', '1995-02-03', 3, 1, null),
(8, 'MAGGIE SIMPSON', '2000-02-03', 3, 1, null)
;

INSERT INTO departments
(department_id, name, manager_id)
VALUES
(1, 'DEPARTMENT-1', 1),
(2, 'DEPARTMENT-2', 1)
;

INSERT INTO departments_employees
(department_id, employee_id)
VALUES
(1, 2),
(1, 3)
;

INSERT INTO employees_tags
(tag_id, employee_id)
VALUES
(1, 2),
(4, 2),
(2, 3),
(5, 3)
;