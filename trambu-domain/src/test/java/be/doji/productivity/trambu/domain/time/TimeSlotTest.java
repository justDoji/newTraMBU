/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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