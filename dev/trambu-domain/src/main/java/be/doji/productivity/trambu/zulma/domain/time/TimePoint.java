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
package be.doji.productivity.trambu.zulma.domain.time;

import com.google.re2j.Pattern;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;

/**
 * Wrapper class for date/time representation as I am very frustrated with Java's built in
 * representations... I hate Dates, and I hate the LocalDateTime shizzle. I will wrap this, so I can
 * express my frustration at a single point, without having to refactor my code for hours on
 * end....
 */
public class TimePoint {

  private static Clock clock = Clock.systemDefaultZone();

  private static final String BASIC_DATE_PATTERN = "dd/MM/uuuu";
  private static final String BASIC_DATE_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d";

  private static final String BASIC_DATE_TIME_PATTERN = "dd/MM/uuuu HH:mm:ss";
  private static final String BASIC_DATE_TIME_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d";

  public static final String EXTENDED_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss:SSS";
  private static final String EXTENDED_DATE_TIME_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d:\\d\\d\\d";

  public static final String LEGACY_DATE_TIME_PATTERN = "uuuu-MM-dd:HH:mm:ss.SSS";
  private static final String LEGACY_DATE_TIME_REGEX = "\\d\\d\\d\\d-\\d\\d-\\d\\d:\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d";

  private static final Map<Pattern, DateTimeFormatter> DATE_CONVERTERS = createDateConverters();
  private static final Map<Pattern, DateTimeFormatter> DATE_TIME_CONVERTERS = createDateTimeConverters();


  private final LocalDateTime internalRepresentation;


  private TimePoint(LocalDateTime dateTime) {
    this.internalRepresentation = dateTime;
  }

  private static Map<Pattern, DateTimeFormatter> createDateConverters() {
    Map<Pattern, DateTimeFormatter> converters = new HashMap<>();
    converters.put(Pattern.compile(BASIC_DATE_REGEX),
        DateTimeFormatter.ofPattern(BASIC_DATE_PATTERN, Locale.FRANCE));

    return converters;
  }

  private static Map<Pattern, DateTimeFormatter> createDateTimeConverters() {
    Map<Pattern, DateTimeFormatter> converters = new HashMap<>();
    converters.put(Pattern.compile(BASIC_DATE_TIME_REGEX),
        DateTimeFormatter.ofPattern(BASIC_DATE_TIME_PATTERN, Locale.FRANCE));
    converters.put(Pattern.compile(EXTENDED_DATE_TIME_REGEX),
        DateTimeFormatter.ofPattern(EXTENDED_DATE_TIME_PATTERN, Locale.FRANCE));
    converters.put(Pattern.compile(LEGACY_DATE_TIME_REGEX),
        DateTimeFormatter.ofPattern(LEGACY_DATE_TIME_PATTERN, Locale.FRANCE));

    return converters;
  }

  public static TimePoint now() {
    return new TimePoint(LocalDateTime.ofInstant(clock.instant(), clock.getZone()));
  }

  public LocalDateTime toLocalDateTime() {
    return this.internalRepresentation;
  }

  public LocalDate toLocalDate() {
    return this.internalRepresentation.toLocalDate();
  }

  /* Utility Methods */

  public static TimePoint fromString(String timeString) {
    if (StringUtils.isBlank(timeString)) {
      throw new IllegalArgumentException(
          "Could not parse given Date string: No matching parsers found for string [" + timeString
              + "] ");
    }

    for (Entry<Pattern, DateTimeFormatter> entry : DATE_TIME_CONVERTERS.entrySet()) {
      if (entry.getKey().matcher(timeString).matches()) {
        return new TimePoint(LocalDateTime.parse(timeString, entry.getValue()));
      }
    }

    for (Entry<Pattern, DateTimeFormatter> entry : DATE_CONVERTERS.entrySet()) {
      if (entry.getKey().matcher(timeString).matches()) {
        return new TimePoint(LocalDate.parse(timeString, entry.getValue()).atStartOfDay());
      }
    }
    throw new IllegalArgumentException(
        "Could not parse given Date string: No matching parsers found for string [" + timeString
            + "] ");
  }

  public String toString(String pattern) {
    DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern(pattern, Locale.FRANCE);
    return formatter.format(this.toLocalDateTime());
  }

  public static TimePoint fromDate(Date date) {
    if (date == null) {
      return null;
    }
    LocalDateTime localDateTime = LocalDateTime
        .ofInstant(date.toInstant(), ZoneId.of(TimeZone.getDefault().getID()));
    return new TimePoint(localDateTime);

  }

  public static TimePoint fromDateTime(LocalDateTime date) {
    if (date == null) {
      return null;
    }
    return new TimePoint(date);

  }

  public static boolean isBefore(TimePoint toCheck, TimePoint reference) {
    return toCheck != null &&
        reference != null &&
        toCheck.toLocalDateTime().isBefore(reference.toLocalDateTime());
  }

  public static boolean isBeforeOrEqual(TimePoint toCheck, TimePoint reference) {
    return isBefore(toCheck, reference) || isEqual(toCheck, reference);
  }

  private static boolean isEqual(TimePoint toCheck, TimePoint reference) {
    return toCheck != null &&
        reference != null &&
        toCheck.toLocalDateTime().isEqual(reference.toLocalDateTime());
  }

  public static boolean isSameDate(TimePoint start, TimePoint reference) {
    LocalDate o1 = start.toLocalDate();
    LocalDate o2 = reference.toLocalDate();
    return o1.isEqual(o2);
  }

  /**
   * static setter for testing purposes
   */
  public static void setTimePointClock(Clock toSet) {
    clock = toSet;
  }

  public TimePoint add(int amount, TemporalUnit unit) {
    LocalDateTime result = LocalDateTime.from(this.toLocalDateTime().plus(amount, unit));
    return new TimePoint(result);
  }


}
