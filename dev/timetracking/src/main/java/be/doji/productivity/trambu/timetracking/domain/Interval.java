package be.doji.productivity.trambu.timetracking.domain;

import static java.math.BigDecimal.valueOf;
import static java.time.Duration.between;

import be.doji.productivity.trambu.timetracking.domain.time.PointInTime;
import be.doji.productivity.trambu.timetracking.domain.time.TimeService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class Interval {

  private UUID occupationId;
  private PointInTime start;
  private PointInTime end;

  private TimeService timeService;

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

  Interval setOccupationId(UUID identifier) {
    this.occupationId = identifier;
    return this;
  }

  private void setStart(PointInTime start) {
    this.start = start;
  }

  private void setEnd(PointInTime end) {
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
        between(this.start.localDateTime(), calcEnd.localDateTime()).getSeconds());
    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
    return bigDecimal.doubleValue();
  }

  public boolean inProgress() {
    return this.getEnd() == null;
  }

  public void endNow() {
    this.end = timeService.now();
  }
}
