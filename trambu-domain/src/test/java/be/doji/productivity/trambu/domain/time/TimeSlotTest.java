/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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