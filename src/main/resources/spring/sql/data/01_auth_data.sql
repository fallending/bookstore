-- Copyright (C) 2013-2014 Paweł Jojczyk
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.

INSERT INTO USERS (ID, NAME, NOT_EXPIRED, NOT_LOCKED, CREDENTIALS_NOT_EXPIRED, ENABLED, PASSWORD)
	VALUES (101, 'admin', true, true, true, true, '$2a$10$ozecWubX2fbGuYAsCGBXouSbuQLgZwunppdHK9E/yv2vybKLc9KF.');
INSERT INTO USERS (ID, NAME, NOT_EXPIRED, NOT_LOCKED, CREDENTIALS_NOT_EXPIRED, ENABLED, PASSWORD)
	VALUES (102, 'user' , true, true, true, true, '$2a$10$Fk3.fso/yfuoRWpBaQkCZe4TfXA5SEiM282KYS9zLUKbgHjDutEWC');
INSERT INTO USERS (ID, NAME, NOT_EXPIRED, NOT_LOCKED, CREDENTIALS_NOT_EXPIRED, ENABLED, PASSWORD)
	VALUES (103, 'unauthorized' , true, true, true, true, '$2a$10$1RffUm7OqnrfWLAWgihMtujRHJXD2aC611UwolMDP0a5Qkdun/Hsi');

INSERT INTO AUTHORITIES (ID, ROLE) VALUES (201, 'ROLE_ADMIN');
INSERT INTO AUTHORITIES (ID, ROLE) VALUES (202, 'ROLE_USER');
INSERT INTO AUTHORITIES (ID, ROLE) VALUES (203, 'ROLE_JMX');

INSERT INTO USERS_AUTHORITIES (USER_ID, AUTHORITY_ID) VALUES (101, 201);
INSERT INTO USERS_AUTHORITIES (USER_ID, AUTHORITY_ID) VALUES (101, 202);
INSERT INTO USERS_AUTHORITIES (USER_ID, AUTHORITY_ID) VALUES (101, 203);
INSERT INTO USERS_AUTHORITIES (USER_ID, AUTHORITY_ID) VALUES (102, 202);
