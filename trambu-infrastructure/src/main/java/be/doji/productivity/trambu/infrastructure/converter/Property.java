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
package be.doji.productivity.trambu.infrastructure.converter;

import static be.doji.productivity.trambu.domain.time.TimePoint.LEGACY_DATE_TIME_PATTERN;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

final class Property {

  /*Utility classes should not have a public or default constructor */
  private Property() {
    throw new UnsupportedOperationException();
  }

  public static final DateTimeFormatter LEGACY_FORMATTER = DateTimeFormatter
      .ofPattern(LEGACY_DATE_TIME_PATTERN, Locale.FRANCE);

  final class Regex {

    /*Utility classes should not have a public or default constructor */
    private Regex() {
      throw new UnsupportedOperationException();
    }

    static final String DATE = "[0-9\\-\\:\\.]*";
    static final String ANYTHING = "(.*?)";

    static final String TERMINATOR = "(\\s|$)";
    static final String GROUP_START = "\\" + Indicator.GROUP_START;
    static final String GROUP_END = "\\" + Indicator.GROUP_END;

    static final String COMPLETED = "^[" + Indicator.DONE + Indicator.DONE_UPPERCASE + "]";

    static final String TITLE = GROUP_START
        + ANYTHING
        + GROUP_END
        + TERMINATOR;
    static final String DEADLINE = Indicator.DEADLINE
        + DATE
        + TERMINATOR;
    static final String TAG = "\\" + Indicator.TAG
        + GROUP_START
        + ANYTHING
        + GROUP_END
        + TERMINATOR;

    static final String PROJECT = "\\" + Indicator.PROJECT
        + GROUP_START
        + ANYTHING
        + GROUP_END
        + TERMINATOR;


    static final String LOGPOINT_START = Indicator.LOGPOINT_START + DATE + TERMINATOR;
    static final String LOGPOINT_END = Indicator.LOGPOINT_END + DATE + TERMINATOR;
    static final String LOGPOINT_ACTIVITY = Indicator.LOGPOINT_ACTIVITY + ANYTHING + TERMINATOR;
  }

  final class Indicator {


    /*Utility classes should not have a public or default constructor */
    private Indicator() {
      throw new UnsupportedOperationException();
    }

    static final String DONE = "x";
    static final String DONE_UPPERCASE = "X";
    static final String GROUP_START = "[";
    static final String GROUP_END = "]";
    static final String PROJECT = "+";
    static final String TAG = "@";
    static final String DEADLINE = "due:";
    static final String WARNING_PERIOD = "warningPeriod:";
    static final String UUID = "uuid:";

    static final String LOGPOINT_ACTIVITY = "ACTIVITY:";
    static final String LOGPOINT_START = "STARTTIME:";
    static final String LOGPOINT_END = "ENDTIME:";

  }
}

