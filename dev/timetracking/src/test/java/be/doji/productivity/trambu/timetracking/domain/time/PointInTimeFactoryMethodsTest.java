package be.doji.productivity.trambu.timetracking.domain.time;

import java.time.LocalDateTime;
import java.time.Month;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PointInTimeFactoryMethodsTest {

  private static final String DOJI_BIRTHDAY = "18/12/1989";

  @Test
  public void fromString_toLocalDateTime_dateOnly() {

    PointInTime timePoint = PointInTime.parse(DOJI_BIRTHDAY);
    LocalDateTime converted = timePoint.dateTime();

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