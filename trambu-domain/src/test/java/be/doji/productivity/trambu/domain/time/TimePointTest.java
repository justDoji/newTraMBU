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

import static be.doji.productivity.trambu.domain.time.TimePoint.LEGACY_DATE_TIME_PATTERN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TimePoint.class})
public class TimePointTest {

  private static final String DOJI_BIRTHDAY = "18/12/1989";

  private static final LocalDateTime NOW = LocalDateTime.of(2019, 5, 4, 14, 13, 0);

  @Before
  public void setUp() {
    Clock clockMock = PowerMockito.mock(Clock.class);
    TimePoint.setTimePointClock(clockMock);
    PowerMockito.when(clockMock.instant()).thenReturn(NOW.toInstant(ZoneOffset.UTC));
    PowerMockito.when(clockMock.getZone()).thenReturn(ZoneOffset.UTC);
  }

  @Test
  public void fromString_toLocalDateTime_dateOnly() {
    TimePoint timePoint = TimePoint.fromString("18/12/1989");
    LocalDateTime converted = timePoint.toLocalDateTime();
    Assert.assertEquals(1989, converted.getYear());
    Assert.assertEquals(Month.DECEMBER, converted.getMonth());
    Assert.assertEquals(18, converted.getDayOfMonth());
    Assert.assertEquals(0, converted.getHour());
    Assert.assertEquals(0, converted.getMinute());
    Assert.assertEquals(0, converted.getSecond());
  }

  @Test
  public void fromString_toLocalDateTime_withHours() {
    TimePoint timePoint = TimePoint.fromString("18/12/1989 12:00:00");
    LocalDateTime converted = timePoint.toLocalDateTime();
    Assert.assertEquals(1989, converted.getYear());
    Assert.assertEquals(Month.DECEMBER, converted.getMonth());
    Assert.assertEquals(18, converted.getDayOfMonth());
    Assert.assertEquals(12, converted.getHour());
    Assert.assertEquals(0, converted.getMinute());
    Assert.assertEquals(0, converted.getSecond());
  }

  @Test
  public void fromString_toLocalDateTime_full() {
    TimePoint timePoint = TimePoint.fromString("18/12/1989 12:13:14");
    LocalDateTime converted = timePoint.toLocalDateTime();
    Assert.assertEquals(1989, converted.getYear());
    Assert.assertEquals(Month.DECEMBER, converted.getMonth());
    Assert.assertEquals(18, converted.getDayOfMonth());
    Assert.assertEquals(12, converted.getHour());
    Assert.assertEquals(13, converted.getMinute());
    Assert.assertEquals(14, converted.getSecond());
  }

  @Test
  public void isSameDate_sameDates_match() {
    TimePoint day1 = TimePoint.fromString(DOJI_BIRTHDAY);
    TimePoint day2 = TimePoint.fromString(DOJI_BIRTHDAY);
    Assert.assertTrue(TimePoint.isSameDate(day1, day2));
  }

  @Test
  public void isSameDate_differentDates() {
    TimePoint day1 = TimePoint.fromString(DOJI_BIRTHDAY);
    TimePoint day2 = TimePoint.fromString("19/12/1989");
    Assert.assertFalse(TimePoint.isSameDate(day1, day2));
  }

  @Test
  public void isSameDate_differentDates_differentHours() {
    TimePoint day1 = TimePoint.fromString("18/12/1989 12:00:05");
    TimePoint day2 = TimePoint.fromString("19/12/1989 19:35:15");
    Assert.assertFalse(TimePoint.isSameDate(day1, day2));
  }

  @Test
  public void isSameDate_sameDates_differentHours_match() {
    TimePoint day1 = TimePoint.fromString("18/12/1989 12:00:00");
    TimePoint day2 = TimePoint.fromString("18/12/1989 18:30:00");
    Assert.assertTrue(TimePoint.isSameDate(day1, day2));
  }

  @Test
  public void fromString_notADate() {
    assertThatThrownBy(() -> TimePoint.fromString("Jos is machtig"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("No matching parsers");
  }

  @Test
  public void isBefore_startBeforeEnd() {
    TimePoint early = TimePoint.fromString(DOJI_BIRTHDAY);
    TimePoint late = TimePoint.fromString("04/05/2019");

    assertThat(TimePoint.isBefore(early, late)).isTrue();
  }

  @Test
  public void isBefore_endBeforeStart() {
    TimePoint early = TimePoint.fromString(DOJI_BIRTHDAY);
    TimePoint late = TimePoint.fromString("04/05/2019");

    assertThat(TimePoint.isBefore(late, early)).isFalse();
  }

  @Test
  public void isBefore_startIsNull() {
    TimePoint early = null;
    TimePoint late = TimePoint.fromString("04/05/2019");

    assertThat(TimePoint.isBefore(late, early)).isFalse();
  }

  @Test
  public void isBefore_endIsNull() {
    TimePoint early = TimePoint.fromString(DOJI_BIRTHDAY);
    TimePoint late = null;

    assertThat(TimePoint.isBefore(late, early)).isFalse();
  }

  @Test
  public void isBefore_startIsNull_bothNull() {
    TimePoint early = null;
    TimePoint late = null;

    assertThat(TimePoint.isBefore(late, early)).isFalse();
  }

  @Test
  public void isBeforeOrEqual_startBeforeEnd() {
    TimePoint early = TimePoint.fromString(DOJI_BIRTHDAY);
    TimePoint late = TimePoint.fromString("04/05/2019");

    assertThat(TimePoint.isBeforeOrEqual(early, late)).isTrue();
  }

  @Test
  public void isBeforeOrEqual_endBeforeStart() {
    TimePoint early = TimePoint.fromString(DOJI_BIRTHDAY);
    TimePoint late = TimePoint.fromString("04/05/2019");

    assertThat(TimePoint.isBeforeOrEqual(late, early)).isFalse();
  }

  @Test
  public void isBeforeOrEqual_sameDate() {
    TimePoint birthday = TimePoint.fromString(DOJI_BIRTHDAY);

    assertThat(TimePoint.isBeforeOrEqual(birthday, birthday)).isTrue();
  }

  @Test
  public void isBeforeOrEqual_startIsNull() {
    TimePoint early = null;
    TimePoint late = TimePoint.fromString("04/05/2019");

    assertThat(TimePoint.isBeforeOrEqual(late, early)).isFalse();
  }

  @Test
  public void isBeforeOrEqual_endIsNull() {
    TimePoint early = TimePoint.fromString(DOJI_BIRTHDAY);
    TimePoint late = null;

    assertThat(TimePoint.isBeforeOrEqual(late, early)).isFalse();
  }

  @Test
  public void isBeforeOrEqual_startIsNull_bothNull() {
    TimePoint early = null;
    TimePoint late = null;

    assertThat(TimePoint.isBeforeOrEqual(late, early)).isFalse();
  }

  @Test
  public void fromString_legacyNotation() {
    String timeString = "2018-12-05:00:00:00.000";
    TimePoint date = TimePoint.fromString(timeString);
    TimePoint day2 = TimePoint.fromString("05/12/2018 00:00:00");
    Assert.assertTrue(TimePoint.isSameDate(date, day2));

  }

  @Test
  public void now_isToday() {
    assertThat(TimePoint.now().toLocalDateTime()).isEqualTo(NOW);
  }

  @Test
  public void write_returnExpectedString() {
    TimePoint day2 = TimePoint.fromString("05/12/2018 00:00:00");
    String output = day2.write(LEGACY_DATE_TIME_PATTERN);
    Assertions.assertThat(output).isEqualTo("2018-12-05:00:00:00.000");
  }

  @Test
  public void write_inputNotValid_throwsException() {
    TimePoint day2 = TimePoint.fromString("05/12/2018 00:00:00");
    assertThatThrownBy(() -> day2.write("this is not a date pattern"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @After
  public void cleanUp() {
    TimePoint.setTimePointClock(Clock.systemDefaultZone());
  }
}
