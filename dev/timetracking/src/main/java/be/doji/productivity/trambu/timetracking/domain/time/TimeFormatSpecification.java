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

import static be.doji.productivity.trambu.timetracking.domain.time.TimeFormat.BASIC_DATE_PATTERN;
import static be.doji.productivity.trambu.timetracking.domain.time.TimeFormat.BASIC_DATE_REGEX;
import static be.doji.productivity.trambu.timetracking.domain.time.TimeFormat.BASIC_DATE_TIME_PATTERN;
import static be.doji.productivity.trambu.timetracking.domain.time.TimeFormat.BASIC_DATE_TIME_REGEX;
import static be.doji.productivity.trambu.timetracking.domain.time.TimeFormat.EXTENDED_DATE_TIME_PATTERN;
import static be.doji.productivity.trambu.timetracking.domain.time.TimeFormat.EXTENDED_DATE_TIME_REGEX;
import static be.doji.productivity.trambu.timetracking.domain.time.TimeFormat.LEGACY_DATE_TIME_PATTERN;
import static be.doji.productivity.trambu.timetracking.domain.time.TimeFormat.LEGACY_DATE_TIME_REGEX;

import com.google.re2j.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

class TimeFormatSpecification {

  private static TimeFormatSpecification BASIC_DATE = fromDate(BASIC_DATE_REGEX,
      BASIC_DATE_PATTERN);
  private static TimeFormatSpecification BASIC_DATETIME = fromDateTime(
      BASIC_DATE_TIME_REGEX, BASIC_DATE_TIME_PATTERN);
  static TimeFormatSpecification EXTENDED_DATE_TIME = fromDateTime(
      EXTENDED_DATE_TIME_REGEX, EXTENDED_DATE_TIME_PATTERN);
  private static TimeFormatSpecification LEGACY_DATETIME = fromDateTime(
      LEGACY_DATE_TIME_REGEX, LEGACY_DATE_TIME_PATTERN);

  private static List<TimeFormatSpecification> converters = Arrays
      .asList(BASIC_DATE, BASIC_DATETIME, EXTENDED_DATE_TIME, LEGACY_DATETIME);

  static PointInTime parse(String source) {
    return converters.stream().filter(spec -> spec.isApplicable(source))
        .findFirst()
        .flatMap(spec -> spec.parseInner(source))
        .map(PointInTime::new)
        .orElseThrow(() -> couldNotParseException(source));
  }


  private final boolean withTime;
  private Pattern regex;
  private DateTimeFormatter formatter;

  private static TimeFormatSpecification fromDate(String regex, String formatter) {
    return new TimeFormatSpecification(regex, formatter, false);
  }

  private static TimeFormatSpecification fromDateTime(String regex, String formatter) {
    return new TimeFormatSpecification(regex, formatter, true);
  }

  private TimeFormatSpecification(String regex, String formatString, boolean includeTime) {
    this.regex = Pattern.compile(regex);
    this.formatter = DateTimeFormatter.ofPattern(formatString, Locale.FRANCE);
    this.withTime = includeTime;
  }


  private Optional<LocalDateTime> parseInner(String toParse) {
    if (!this.isApplicable(toParse)) {
      throw couldNotParseException(toParse);
    }

    return Optional.of(withTime ? LocalDateTime.parse(toParse, formatter)
        : LocalDate.parse(toParse, formatter).atStartOfDay());
  }

  private static IllegalArgumentException couldNotParseException(String toParse) {
    return new IllegalArgumentException(String.format(
        "Could not parse given Date string: No matching parsers found for string [ %s] ",
        toParse));
  }

  public String string(PointInTime pointInTime) {
    return formatter.format(pointInTime.dateTime());
  }

  private boolean isApplicable(String dateTimeString) {
    return regex.matches(dateTimeString);
  }
}
