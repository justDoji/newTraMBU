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
package be.doji.productivity.trambu.zulma.domain.time;


import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TimeSlotTest {

  private static final LocalDateTime NOW = LocalDateTime.of(2019, 12, 4, 14, 30, 0);

  @Before
  public void setUp() {
    Clock clockMock = mock(Clock.class);
    TimeSlot.setTimePointClock(clockMock);
    TimePoint.setTimePointClock(clockMock);
    when(clockMock.instant()).thenReturn(NOW.toInstant(ZoneOffset.UTC));
    when(clockMock.getZone()).thenReturn(ZoneOffset.UTC);
  }

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

  @Test
  public void getTimeInHours() {
    Calendar startTime = new GregorianCalendar();
    startTime.set(2019, Calendar.DECEMBER, 10, 12, 0, 0);

    Calendar endTime = new GregorianCalendar();
    endTime.set(2019, Calendar.DECEMBER, 18, 12, 30, 0);

    TimeSlot slot = TimeSlot.between(startTime.getTime(), endTime.getTime());

    assertThat(slot.getTimeSpanInHours()).isEqualTo(192.5);
  }

  @Test
  public void getOverlapWithToday_EndInFuture() {
    // Clock is set to 4/12/2019 14:30:00

    Calendar startTime = new GregorianCalendar();
    startTime.set(2019, Calendar.DECEMBER, 18, 12, 0, 0);

    Calendar endTime = new GregorianCalendar();
    endTime.set(2019, Calendar.DECEMBER, 3, 12, 0, 0);

    TimeSlot slot = TimeSlot.between(startTime.getTime(), endTime.getTime());
    TimeSlot overlap = slot.overlapWithToday();
    assertThat(overlap.getTimeSpanInHours()).isEqualTo(14.5);
  }

  @After
  public void cleanUp() {
    TimePoint.setTimePointClock(Clock.systemDefaultZone());
    TimeSlot.setTimePointClock(Clock.systemDefaultZone());
  }

}