--
-- TraMBU - an open time management tool
--
--     Copyright (C) 2019  Stijn Dejongh
--
--     This program is free software: you can redistribute it and/or modify
--     it under the terms of the GNU Affero General Public License as
--     published by the Free Software Foundation, either version 3 of the
--     License, or (at your option) any later version.
--
--     This program is distributed in the hope that it will be useful,
--     but WITHOUT ANY WARRANTY; without even the implied warranty of
--     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--     GNU Affero General Public License for more details.
--
--     You should have received a copy of the GNU Affero General Public License
--     along with this program.  If not, see <https://www.gnu.org/licenses/>.
--
--     For further information on usage, or licensing, contact the author
--     through his github profile: https://github.com/justDoji
--
--

DROP TABLE ACTIVITY IF EXISTS;
CREATE TABLE ACTIVITY (ID NUMBER(48) NOT NULL,COMPLETED NUMBER(1) NOT NULL,TITLE VARCHAR(255) NOT NULL, DEADLINE VARCHAR(48), REFERENCE_KEY VARCHAR(48) , PRIMARY KEY(ID));
CREATE SEQUENCE IF NOT EXISTS SEQ_ACTIVITY START WITH 1 INCREMENT BY 1;