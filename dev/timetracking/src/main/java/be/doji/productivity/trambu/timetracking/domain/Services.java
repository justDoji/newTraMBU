package be.doji.productivity.trambu.timetracking.domain;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * Given the split of interfaces and implementation between domain/impl packages,
 * it is not trivial to access the Domain Services from our Domain Objects.
 * This class is a centralized DomainService lookup.
 *
 * This is to be the only spot in the domain where Spring annotations are permitted.
 *
 * TODO: get rid of this
 */
@Configurable
public final class Services {

  private static TimeService timeService;
  private TimeService timeServiceInstance;

  public Services(@Autowired TimeService timeService) {
    this.timeServiceInstance = timeService;
  }

  @PostConstruct
  private void initStaticTimeService() {
    timeService = this.timeServiceInstance;
  }

  static void setTimeService(TimeService newTimeService) {
    timeService = newTimeService;
  }

  public static TimeService time() {
    return timeService;
  }
}
