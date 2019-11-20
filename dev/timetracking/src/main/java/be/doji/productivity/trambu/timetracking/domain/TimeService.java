package be.doji.productivity.trambu.timetracking.domain;

import java.time.Clock;
import java.time.LocalDateTime;

public interface TimeService {

  Clock getSharedClock();

  default PointInTime now() {
    return new PointInTime(LocalDateTime.now(getSharedClock()));
  }
}


