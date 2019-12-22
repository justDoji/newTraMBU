/**
 * TraMBU - an open time management tool
 *
 *     Copyright (C) 2019  Stijn Dejongh
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     For further information on usage, or licensing, contact the author
 *     through his github profile: https://github.com/justDoji
 */
package be.doji.productivity.trambu.timetracking.domain.time;

public final class TimeFormat {

  static final String BASIC_DATE_PATTERN = "dd/MM/uuuu";
  static final String BASIC_DATE_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d";
  static final String BASIC_DATE_TIME_PATTERN = "dd/MM/uuuu HH:mm:ss";
  static final String BASIC_DATE_TIME_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d";
  static final String EXTENDED_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss:SSS";
  static final String EXTENDED_DATE_TIME_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d:\\d\\d\\d";
  static final String LEGACY_DATE_TIME_PATTERN = "uuuu-MM-dd:HH:mm:ss.SSS";
  static final String LEGACY_DATE_TIME_REGEX = "\\d\\d\\d\\d-\\d\\d-\\d\\d:\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d";

  private TimeFormat() {
    throw new UnsupportedOperationException("Utility classes should not be instantiated");
  }
}
