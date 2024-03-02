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

ALTER SEQUENCE city_seq RESTART WITH 20;


INSERT INTO banks (bank_id, name) VALUES
(1, 'BANK 1'),
(2, 'BANK 2')
;

ALTER SEQUENCE bank_seq RESTART WITH 10;


INSERT INTO banks_accounts (account_id, account_number, account_type, bank_id) VALUES
(1, 'SS00000001', 1, 1),
(2, 'SS00000002', 1, 1),
(3, 'OO00000003', 0, 2),
(4, 'OO00000004', 0, 2),
(5, 'OO00000005', 0, 2),
(6, 'OO00000006', 0, 2),
(7, 'OO00000007', 0, 2),
(8, 'OO00000008', 0, 2)
;

ALTER SEQUENCE bank_account_seq RESTART WITH 10;


INSERT INTO employees
(employee_id, global_id, names, birthdate, city_id, status, bank_account_id)
VALUES
(1, 'e71259c0-7438-4752-be3a-1a664de706e8', 'EMPLOYEE-1', '1980-02-03', 1, 1, 1),
(2, '81298f47-0af9-4604-ade0-8ecc6d151921', 'EMPLOYEE-2', '1980-02-03', 1, 1, 2),
(3, '75f60d67-587e-46bc-b223-9c48356114fc', 'EMPLOYEE-3', '1980-02-03', 1, 1, 3),
(4, '7ccc6f9a-b4ac-4c54-85d2-59fd4ccfe221', 'HOMER SIMPSON', '1980-02-03', 3, 1, 4),
(5, '1ed1d31f-3cad-4873-aabc-45ceb8d8ffee', 'MARGE SIMPSON', '1980-02-03', 3, 1, 5),
(6, '5a61f181-b88c-4974-a6a3-3416ca337706', 'BART SIMPSON', '1990-02-03', 3, 1, 6),
(7, '95a0066a-ce58-4eae-9f7a-6cf2dcd85c1a', 'LISA SIMPSON', '1995-02-03', 3, 1, 7),
(8, '2c2ea326-751e-4870-93a5-d25fd29c5ec1', 'MAGGIE SIMPSON', '2000-02-03', 3, 1, 8)
;

ALTER SEQUENCE employee_seq RESTART WITH 10;


INSERT INTO departments
(department_id, name, manager_id)
VALUES
(1, 'DEPARTMENT-1', 1),
(2, 'DEPARTMENT-2', 1)
;

ALTER SEQUENCE department_seq RESTART WITH 10;


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