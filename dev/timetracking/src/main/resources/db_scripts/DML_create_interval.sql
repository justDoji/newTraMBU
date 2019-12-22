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

DROP TABLE INTERVAL IF EXISTS;
CREATE TABLE INTERVAL (INTERVAL_ID VARCHAR(48) NOT NULL,CORRELATION_ID VARCHAR(48) NOT NULL,START VARCHAR(255) NOT NULL, END VARCHAR(255),PRIMARY KEY(INTERVAL_ID), FOREIGN KEY(CORRELATION_ID) references OCCUPATION(CORRELATION_ID));