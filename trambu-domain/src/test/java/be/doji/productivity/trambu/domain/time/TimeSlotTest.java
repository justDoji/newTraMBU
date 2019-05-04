package be.doji.productivity.trambu.domain.time;


import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

public class TimeSlotTest {

  @Test
  public void between_creation_normalUsage() {
    TimePoint dayOne = TimePoint.fromString("04/05/2019");
    TimePoint dayTwo = TimePoint.fromString("05/05/2019");

    TimeSlot between = TimeSlot.between(dayOne, dayTwo);

    assertThat(between.getStart()).isEqualTo(dayOne);
    assertThat(between.getEnd()).isEqualTo(dayTwo);
  }

  @Test
  public void between_creation_invertedDates() {
    TimePoint dayOne = TimePoint.fromString("04/05/2019");
    TimePoint dayTwo = TimePoint.fromString("05/05/2019");

    TimeSlot between = TimeSlot.between(dayTwo, dayOne);

    assertThat(between.getStart()).isEqualTo(dayOne);
    assertThat(between.getEnd()).isEqualTo(dayTwo);
  }

  @Test
  public void contains_timePointInRange() {
    TimePoint dayOne = TimePoint.fromString("04/05/2019 01:00:00");
    TimePoint dayTwo = TimePoint.fromString("05/05/2019 23:59:00");
    TimeSlot between = TimeSlot.between(dayTwo, dayOne);

    assertThat(between.contains(TimePoint.fromString("05/05/2019 12:00:00"))).isTrue();
  }

  @Test
  public void contains_timePointNotInRange() {
    TimePoint dayOne = TimePoint.fromString("04/05/2019 01:00:00");
    TimePoint dayTwo = TimePoint.fromString("05/05/2019 23:59:00");
    TimeSlot between = TimeSlot.between(dayTwo, dayOne);

    assertThat(between.contains(TimePoint.fromString("06/05/2019 12:00:00"))).isFalse();
  }

  @Test
  public void contains_timePointOnStartEdge() {
    TimePoint dayOne = TimePoint.fromString("04/05/2019 01:00:00");
    TimePoint dayTwo = TimePoint.fromString("05/05/2019 23:59:00");
    TimeSlot between = TimeSlot.between(dayTwo, dayOne);

    assertThat(between.contains(dayOne)).isTrue();
  }

  @Test
  public void contains_timePointOnEndEdge() {
    TimePoint dayOne = TimePoint.fromString("04/05/2019 01:00:00");
    TimePoint dayTwo = TimePoint.fromString("05/05/2019 23:59:00");
    TimeSlot between = TimeSlot.between(dayTwo, dayOne);

    assertThat(between.contains(dayOne)).isTrue();
  }

}