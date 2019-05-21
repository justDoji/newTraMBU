/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package be.doji.productivity.trambu.domain.time;

public class TimeSlot {

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
}
