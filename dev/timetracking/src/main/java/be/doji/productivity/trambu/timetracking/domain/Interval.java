package be.doji.productivity.trambu.timetracking.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.UUID;

public class Interval {

  private UUID occupationId;
  private PointInTime start;
  private PointInTime end;

  Interval(UUID occupationId, PointInTime start,
      PointInTime end) {
    this.occupationId = occupationId;
    this.start = start;
    this.end = end;
  }

  public Interval(PointInTime start, PointInTime end) {
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
    BigDecimal bigDecimal = BigDecimal.valueOf(getTimeSpanInSeconds() / (60 * 60));
    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
    return bigDecimal.doubleValue();
  }

  public double getTimeSpanInSeconds() {
    PointInTime calcEnd = this.end == null ? PointInTime.now() : this.end;
    BigDecimal bigDecimal = BigDecimal.valueOf(
        Duration.between(this.start.toLocalDateTime(), calcEnd.toLocalDateTime()).getSeconds());
    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
    return bigDecimal.doubleValue();
  }
}
