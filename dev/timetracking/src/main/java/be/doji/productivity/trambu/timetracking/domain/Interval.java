package be.doji.productivity.trambu.timetracking.domain;

import static be.doji.productivity.trambu.timetracking.domain.Services.*;
import static java.math.BigDecimal.valueOf;
import static java.time.Duration.between;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

public class Interval {

  private UUID occupationId;
  private PointInTime start;
  private PointInTime end;

  private TimeService timeService;

  Interval(
      UUID occupationId,
      PointInTime start,
      PointInTime end) {
    this.occupationId = occupationId;
    this.start = start;
    this.end = end;
  }

  Interval(
      UUID occupationId,
      PointInTime start) {
    this.occupationId = occupationId;
    this.start = start;
  }

  Interval(PointInTime start, PointInTime end) {
    this(null, start, end);
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
    PointInTime calcEnd = this.end == null ? time().now() : this.end;
    BigDecimal bigDecimal = valueOf(
        between(this.start.localDateTime(), calcEnd.localDateTime()).getSeconds());
    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
    return bigDecimal.doubleValue();
  }

  public boolean inProgress() {
    return this.getEnd() == null;
  }

  public void endNow() {
    this.end = time().now();
  }
}
