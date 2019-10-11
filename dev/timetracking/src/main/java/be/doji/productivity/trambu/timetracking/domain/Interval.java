package be.doji.productivity.trambu.timetracking.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.UUID;

class Interval {

  private UUID occupationId;
  private PointInTime start;
  private PointInTime end;

  Interval(UUID occupationId, PointInTime start,
      PointInTime end) {
    this.occupationId = occupationId;
    this.start = start;
    this.end = end;
  }

  UUID getOccupationId() {
    return occupationId;
  }

  private Interval setRootCorrelationId(UUID identifier) {
    this.occupationId = identifier;
    return this;
  }

  void setStart(PointInTime start) {
    this.start = start;
  }

  void setEnd(PointInTime end) {
    this.end = end;
  }

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
