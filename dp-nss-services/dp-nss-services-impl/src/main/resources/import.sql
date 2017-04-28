--v databazi (dpp) je nutne mit krome defaultniho public schematu take schema 'global'
--CREATE SCHEMA IF NOT EXISTS global;

delete from global.person_role;
delete from global.roles;

delete from global.person_time_table;
delete from global.time_tables;

delete from global.persons;

--Users
-- admin/admin ---- user/admin
INSERT INTO global.persons(id, username, password, token, passwordChangeRequired) VALUES (1, 'admin', '$2a$11$pFWVM2513P5FZWXX5nS0t.Z6llSDroVrDNFPmg3YoZtUX36N3zAgy', 'x', false);
INSERT INTO global.persons(id, username, password, token, passwordChangeRequired) VALUES (2, 'user', '$2a$11$pFWVM2513P5FZWXX5nS0t.Z6llSDroVrDNFPmg3YoZtUX36N3zAgy', 'y', false);
ALTER SEQUENCE global.persons_id_seq RESTART WITH 30;

--ROLES
INSERT INTO global.roles(type) VALUES ('ADMIN');
INSERT INTO global.roles(type) VALUES ('USER');

--user roles
INSERT INTO global.person_role(person_id, role_id) VALUES (1, 'ADMIN');
INSERT INTO global.person_role(person_id, role_id) VALUES (1, 'USER');
INSERT INTO global.person_role(person_id, role_id) VALUES (2, 'USER');


--TimeTables
INSERT INTO global.time_tables(id, name, maxTravelTime, valid, synchronizing) VALUES ('pid', 'Pražská integrovaná doprava (PID)', 3, true, false);
INSERT INTO global.time_tables(id, name, maxTravelTime, valid, synchronizing) VALUES ('annapolis', 'Annapolis', 3, true, false);

--user time tables
INSERT INTO global.person_time_table(person_id, time_table_id) VALUES (1, 'pid');
INSERT INTO global.person_time_table(person_id, time_table_id) VALUES (2, 'pid');