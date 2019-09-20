package be.doji.productivity.trambu.timetracking.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class PointInTimeFactoryMethodsTest {

  private static final String DOJI_BIRTHDAY = "18/12/1989";
  private static final LocalDateTime MAYDAY = LocalDateTime.of(2019, 5, 4, 14, 13, 0);
  private Clock clockMock;

  @Before
  public void setUp() {
    clockMock = mock(Clock.class);
    PointInTime.setTimePointClock(clockMock);
    when(clockMock.instant()).thenReturn(MAYDAY.toInstant(ZoneOffset.UTC));
    when(clockMock.getZone()).thenReturn(ZoneOffset.UTC);
  }

  @Test
  public void fromString_toLocalDateTime_dateOnly() {
    PointInTime timePoint = PointInTime.fromString(DOJI_BIRTHDAY);
    LocalDateTime converted = timePoint.toLocalDateTime();

    SoftAssertions assertions = new SoftAssertions();
    assertions.assertThat(converted.getYear()).isEqualTo(1989);
    assertions.assertThat(converted.getMonth()).isEqualTo(Month.DECEMBER);
    assertions.assertThat(converted.getDayOfMonth()).isEqualTo(18);
    assertions.assertThat(converted.getHour()).isEqualTo(0);
    assertions.assertThat(converted.getMinute()).isEqualTo(0);
    assertions.assertThat(converted.getSecond()).isEqualTo(0);
    assertions.assertAll();
  }

}