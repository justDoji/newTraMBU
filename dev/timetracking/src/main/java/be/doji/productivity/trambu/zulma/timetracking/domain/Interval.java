/**
 * TraMBU - an open time management tool
 *
 * Copyright (C) 2019  Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.zulma.timetracking.domain;

import static java.math.BigDecimal.valueOf;
import static java.time.Duration.between;

import be.doji.productivity.trambu.zulma.timetracking.domain.time.PointInTime;
import be.doji.productivity.trambu.zulma.timetracking.domain.time.TimeService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class Interval {

  private UUID occupationId;
  private PointInTime start;
  private PointInTime end;

  private TimeService timeService;
  private UUID intervalId = UUID.randomUUID();

  public Interval() {
  }

  Interval(
      UUID occupationId,
      PointInTime start,
      PointInTime end,
      TimeService timeService) {
    this.occupationId = occupationId;
    this.start = start;
    this.end = end;
    this.timeService = timeService;
  }

  Interval(
      UUID occupationId,
      PointInTime start,
      TimeService timeService) {
    this.occupationId = occupationId;
    this.start = start;
    this.timeService = timeService;
  }

  Interval(PointInTime start,
      PointInTime end,
      TimeService timeService) {
    this(null, start, end, timeService);
  }

  public Interval(UUID identifier, TimeService timeService) {
    this(identifier, timeService.now(), timeService);
  }

  public UUID getOccupationId() {
    return occupationId;
  }

  public Interval setOccupationId(UUID identifier) {
    this.occupationId = identifier;
    return this;
  }

  public void setStart(PointInTime start) {
    this.start = start;
  }

  public void setEnd(PointInTime end) {
    this.end = end;
  }

  public PointInTime getStart() { return start; }

  public PointInTime getEnd() { return end; }

  public double getTimeSpanInHours() {
    BigDecimal bigDecimal = valueOf(getTimeSpanInSeconds() / (60 * 60));
    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
    return bigDecimal.doubleValue();
  }

  public double getTimeSpanInSeconds() {
    PointInTime calcEnd = this.end == null ? timeService.now() : this.end;
    BigDecimal bigDecimal = valueOf(
        between(this.start.dateTime(), calcEnd.dateTime()).getSeconds());
    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
    return bigDecimal.doubleValue();
  }

  public boolean inProgress() {
    return this.getEnd() == null;
  }

  public void endNow() {
    this.end = timeService.now();
  }

  public void setIntervalId(UUID intervalId) {
    this.intervalId = intervalId;
  }

  public UUID getIntervalId() {
    return intervalId;
  }
}
