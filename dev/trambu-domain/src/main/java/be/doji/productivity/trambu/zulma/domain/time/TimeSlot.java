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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeSlot {

  private static Clock clock = Clock.systemDefaultZone();

  private final TimePoint start;
  private final TimePoint end;

  public TimeSlot(TimePoint plannedStart, TimePoint plannedEnd) {
    this.start = plannedStart;
    this.end = plannedEnd;
  }

  public TimePoint getStart() {
    return this.start;
  }

  public TimePoint getEnd() {
    return this.end;
  }

  public boolean startsOn(TimePoint reference) {
    return TimePoint.isSameDate(this.start, reference);
  }

  public boolean endsOn(TimePoint reference) {
    return TimePoint.isSameDate(this.end, reference);
  }

  public static TimeSlot between(TimePoint from, TimePoint to) {
    return TimePoint.isBefore(from, to) ? new TimeSlot(from, to) : new TimeSlot(to, from);
  }

  public boolean contains(TimePoint toCheck) {
    return TimePoint.isBeforeOrEqual(toCheck, end) && TimePoint.isBeforeOrEqual(start, toCheck);
  }

  public static TimeSlot between(Date from, Date to) {
    return between(TimePoint.fromDate(from), TimePoint.fromDate(to));
  }

  public double getTimeSpanInHours() {
    BigDecimal bigDecimal = BigDecimal.valueOf(getTimeSpanInSeconds() / (60 * 60));
    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
    return bigDecimal.doubleValue();
  }

  public double getTimeSpanInSeconds() {
    TimePoint calcEnd = this.getEnd() == null ? TimePoint.now() : this.getEnd();
    BigDecimal bigDecimal = BigDecimal.valueOf(Duration.between(this.getStart().toLocalDateTime(), calcEnd.toLocalDateTime()).getSeconds());
    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
    return bigDecimal.doubleValue();
  }

  /**
   * static setter for testing purposes
   */
  public static void setTimePointClock(Clock toSet) {
    clock = toSet;
  }

  public TimeSlot overlapWithToday() {
    TimePoint calcEnd =
        TimePoint.isBeforeOrEqual(TimePoint.now(), this.getEnd()) ? TimePoint.now() : this.getEnd();
    LocalDateTime startOfToday = LocalDateTime.now(clock).truncatedTo(ChronoUnit.DAYS).withHour(0)
        .withMinute(0).withSecond(0);
    TimePoint calcStart = this.getStart().toLocalDateTime().isBefore(startOfToday) ? TimePoint
        .fromDateTime(startOfToday) : this.getStart();
    return new TimeSlot(calcStart, calcEnd);
  }
}
