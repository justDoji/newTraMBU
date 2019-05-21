--
-- GNU Affero General Public License Version 3
--
-- Copyright (c) 2019 Stijn Dejongh
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU Affero General Public License for more details.
--
-- You should have received a copy of the GNU Affero General Public License
-- along with this program. If not, see <http://www.gnu.org/licenses/>.
--

DROP TABLE ACTIVITY_PROJECT IF EXISTS;
CREATE TABLE ACTIVITY_PROJECT (ID NUMBER(48) NOT NULL,FK_ACTIVITY_ID NUMBER(48) NOT NULL,VALUE VARCHAR(255) NOT NULL, PRIMARY KEY(ID), FOREIGN KEY (FK_ACTIVITY_ID) REFERENCES ACTIVITY(ID));
CREATE SEQUENCE IF NOT EXISTS SEQ_PROJECT START WITH 1 INCREMENT BY 1;