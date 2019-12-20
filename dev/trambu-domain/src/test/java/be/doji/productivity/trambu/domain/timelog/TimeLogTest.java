/**
 * TraMBU - an open time management tool
 *
 *     Copyright (C) 2019  Stijn Dejongh
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     For further information on usage, or licensing, contact the author
 *     through his github profile: https://github.com/justDoji
 */
package be.doji.productivity.trambu.domain.timelog;

import static org.assertj.core.api.Assertions.assertThat;


import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.domain.time.TimeSlot;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TimeLogTest {

  @Test
  public void construction() {
    Activity activity = Activity.builder()
        .title("Start design practise")
        .plannedStartAt(TimePoint.fromString("01/05/2019"))
        .plannedEndAt(TimePoint.fromString("31/05/2019"))
        .build();
    TimeLog log = new TimeLog(activity);
    Assertions.assertThat(log.getActivity()).isEqualTo(activity);
    assertThat(log.getSlots()).hasSize(0);
  }

  @Test
  public void registerTime_manual() {
    TimeLog log = new TimeLog(
        Activity.builder()
            .title("Start design practise")
            .build()
    );

    log.addLogPoint(TimeSlot.between(
        TimePoint.fromString("04/05/2019 07:45:00"),
        TimePoint.fromString("04/05/2019 17:53:00")));

    assertThat(log.getSlots()).hasSize(1);
  }

}