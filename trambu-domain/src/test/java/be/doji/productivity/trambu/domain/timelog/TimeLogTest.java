package be.doji.productivity.trambu.domain.timelog;

import static org.assertj.core.api.Assertions.assertThat;


import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.domain.time.TimeSlot;
import org.junit.Test;

public class TimeLogTest {

  @Test
  public void construction() {
    Activity activity = Activity.builder()
        .name("Start design practise")
        .plannedStartAt(TimePoint.fromString("01/05/2019"))
        .plannedEndAt(TimePoint.fromString("31/05/2019"))
        .build();
    TimeLog log = new TimeLog(activity);
    assertThat(log.getActivity()).isEqualTo(activity);
    assertThat(log.getSlots()).hasSize(0);
  }

  @Test
  public void registerTime_manual() {
    TimeLog log = new TimeLog(
        Activity.builder()
            .name("Start design practise")
            .build()
    );

    log.addLogPoint(TimeSlot.between(
        TimePoint.fromString("04/05/2019 07:45:00"),
        TimePoint.fromString("04/05/2019 17:53:00")));

    assertThat(log.getSlots()).hasSize(1);
  }

}