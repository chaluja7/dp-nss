--v databazi (dpp) je nutne mit krome defaultniho public schematu take schema 'global'
--CREATE SCHEMA IF NOT EXISTS global;

--Users
INSERT INTO global.persons(username, password) VALUES ('admin', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb');

--TimeTables
INSERT INTO global.time_tables(id, name, valid) VALUES ('pid', 'Pražská integrovaná doprava (PID)', true);
INSERT INTO global.time_tables(id, name, valid) VALUES ('annapolis', 'Annapolis', true);