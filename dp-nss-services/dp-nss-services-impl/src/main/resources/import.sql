--v databazi (dpp) je nutne mit krome defaultniho public schematu take schema 'global'
--CREATE SCHEMA IF NOT EXISTS global;

--Users
TRUNCATE global.persons CASCADE;
-- admin/admin
INSERT INTO global.persons(id, username, password, token) VALUES (1, 'admin', '$2a$11$pFWVM2513P5FZWXX5nS0t.Z6llSDroVrDNFPmg3YoZtUX36N3zAgy', 'x');
ALTER SEQUENCE global.persons_id_seq RESTART WITH 30;

--ROLES
TRUNCATE global.roles CASCADE;
INSERT INTO global.roles(type) VALUES ('ADMIN');
INSERT INTO global.roles(type) VALUES ('USER');

--user roles
TRUNCATE global.person_role CASCADE;
INSERT INTO global.person_role(person_id, role_id) VALUES (1, 'ADMIN');
INSERT INTO global.person_role(person_id, role_id) VALUES (1, 'USER');


--TimeTables
TRUNCATE global.time_tables CASCADE;
INSERT INTO global.time_tables(id, name, valid) VALUES ('pid', 'Pražská integrovaná doprava (PID)', true);
INSERT INTO global.time_tables(id, name, valid) VALUES ('annapolis', 'Annapolis', true);