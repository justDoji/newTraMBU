package be.doji.productivity.trambu.timetracking.domain.time;

import be.doji.productivity.trambu.kernel.annotations.ValueObject;
import java.time.LocalDateTime;

@ValueObject
public final class PointInTime {

  private final LocalDateTime internalDateTime;

  public PointInTime(LocalDateTime temporal) {
    this.internalDateTime = temporal;
  }

  public static PointInTime parse(String toParse) {
    return TimeFormatSpecification.parse(toParse);
  }

  public LocalDateTime dateTime() {
    return this.internalDateTime;
  }
}
