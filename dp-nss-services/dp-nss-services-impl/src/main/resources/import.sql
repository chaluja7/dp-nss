--v databazi (dpp) je nutne mit krome defaultniho public schematu take schema 'global'
--CREATE SCHEMA IF NOT EXISTS global;

TRUNCATE global.person_role CASCADE;
TRUNCATE global.roles CASCADE;

TRUNCATE global.person_time_table CASCADE;
TRUNCATE global.time_tables CASCADE;

TRUNCATE global.persons CASCADE;

--Users
-- admin/admin ---- user/admin
INSERT INTO global.persons(id, username, password, token) VALUES (1, 'admin', '$2a$11$pFWVM2513P5FZWXX5nS0t.Z6llSDroVrDNFPmg3YoZtUX36N3zAgy', 'x');
INSERT INTO global.persons(id, username, password, token) VALUES (2, 'user', '$2a$11$pFWVM2513P5FZWXX5nS0t.Z6llSDroVrDNFPmg3YoZtUX36N3zAgy', 'y');
ALTER SEQUENCE global.persons_id_seq RESTART WITH 30;

--ROLES
INSERT INTO global.roles(type) VALUES ('ADMIN');
INSERT INTO global.roles(type) VALUES ('USER');

--user roles
INSERT INTO global.person_role(person_id, role_id) VALUES (1, 'ADMIN');
INSERT INTO global.person_role(person_id, role_id) VALUES (1, 'USER');
INSERT INTO global.person_role(person_id, role_id) VALUES (2, 'USER');


--TimeTables
INSERT INTO global.time_tables(id, name, valid) VALUES ('pid', 'Pražská integrovaná doprava (PID)', true);
INSERT INTO global.time_tables(id, name, valid) VALUES ('annapolis', 'Annapolis', true);

--user time tables
INSERT INTO global.person_time_table(person_id, time_table_id) VALUES (1, 'pid');
INSERT INTO global.person_time_table(person_id, time_table_id) VALUES (2, 'pid');