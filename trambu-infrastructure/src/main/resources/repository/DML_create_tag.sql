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

DROP TABLE ACTIVITY_TAG IF EXISTS;
CREATE TABLE ACTIVITY_TAG (ID NUMBER(48) NOT NULL,FK_ACTIVITY_ID NUMBER(48) NOT NULL,VALUE VARCHAR(255) NOT NULL, PRIMARY KEY(ID), FOREIGN KEY (FK_ACTIVITY_ID) REFERENCES ACTIVITY(ID));
CREATE SEQUENCE IF NOT EXISTS SEQ_TAG START WITH 1 INCREMENT BY 1;