package be.doji.productivity.trambu.timetracking.infra;

import be.doji.productivity.trambu.timetracking.domain.time.TimeService;
import java.time.Clock;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

  private final Clock sharedClock;

  public TimeServiceImpl() {
    sharedClock = Clock.systemDefaultZone();
  }

  public TimeServiceImpl(Clock sharedClock) {
    this.sharedClock = sharedClock;
  }

  @Override
  public Clock getSharedClock() {
    return sharedClock;
  }

}
