package be.doji.productivity.trambu.timetracking.domain;

import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PointInTimeFactoryMethodsTest {

  private static final String DOJI_BIRTHDAY = "18/12/1989";

  @Test
  public void fromString_toLocalDateTime_dateOnly() {

    PointInTime timePoint = PointInTime.fromString(DOJI_BIRTHDAY);
    LocalDateTime converted = timePoint.localDateTime();

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