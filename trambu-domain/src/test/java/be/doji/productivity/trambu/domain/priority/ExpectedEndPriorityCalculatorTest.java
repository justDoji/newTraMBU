package be.doji.productivity.trambu.domain.priority;

import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.activity.Importance;
import be.doji.productivity.trambu.domain.time.TimePoint;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TimePoint.class})
public class ExpectedEndPriorityCalculatorTest {

  private static final LocalDateTime NOW = LocalDateTime.of(2019, 5, 4, 14, 13, 0);

  @Before
  public void setUp() {
    Clock clockMock = PowerMockito.mock(Clock.class);
    TimePoint.setTimePointClock(clockMock);
    PowerMockito.when(clockMock.instant()).thenReturn(NOW.toInstant(ZoneOffset.UTC));
    PowerMockito.when(clockMock.getZone()).thenReturn(ZoneOffset.UTC);
  }

  @Test
  public void calculate_happyFlow() {
    Activity activity = Activity.builder()
        .name("Start design practise")
        .importance(Importance.NORMAL)
        .deadline(TimePoint.fromString("20/12/2020"))
        .plannedEndAt(TimePoint.fromString("20/12/2020"))
        .plannedStartAt(TimePoint.fromString("01/01/2015"))
        .build();
    ExpectedEndPriorityCalculator calculator = new ExpectedEndPriorityCalculator();
    Priority priority = calculator.calculatePriority(activity);

    assertThat(priority).isNotNull();
    assertThat(priority).isEqualTo(Priority.LOW);
  }

  //TODO: exception flows

  @After
  public void cleanUp() {
    TimePoint.setTimePointClock(Clock.systemDefaultZone());
  }

}