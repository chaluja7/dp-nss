--v databazi (dpp) je nutne mit krome defaultniho public schematu take schema 'global'
--CREATE SCHEMA IF NOT EXISTS global;

delete from global.person_role;
delete from global.roles;

delete from global.person_time_table;
delete from global.time_tables;

delete from global.persons;

--Users
-- admin/???
INSERT INTO global.persons(id, username, password, token, passwordChangeRequired) VALUES (1, 'admin', '$2a$04$o2Se8bSvktF5HtVTNAzqMOk1tyr/bAEsBILODyDWbTUUjYaum2zDG', null, false);
ALTER SEQUENCE global.persons_id_seq RESTART WITH 30;

--ROLES
INSERT INTO global.roles(type) VALUES ('ADMIN');
INSERT INTO global.roles(type) VALUES ('USER');

--user roles
INSERT INTO global.person_role(person_id, role_id) VALUES (1, 'ADMIN');
INSERT INTO global.person_role(person_id, role_id) VALUES (1, 'USER');

--TimeTables
INSERT INTO global.time_tables(id, name, maxTravelTime, valid, synchronizing) VALUES ('pid', 'Pražská integrovaná doprava (PID)', 6, true, false);
INSERT INTO global.time_tables(id, name, maxTravelTime, valid, synchronizing) VALUES ('vedouci', 'Jízdní řád vedoucí', 6, true, false);
INSERT INTO global.time_tables(id, name, maxTravelTime, valid, synchronizing) VALUES ('oponent', 'Jízdní řád oponent', 6, true, false);

--user time tables
INSERT INTO global.person_time_table(person_id, time_table_id) VALUES (1, 'pid');
INSERT INTO global.person_time_table(person_id, time_table_id) VALUES (1, 'vedouci');
INSERT INTO global.person_time_table(person_id, time_table_id) VALUES (1, 'oponent');