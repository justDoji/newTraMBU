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
package be.doji.productivity.trambu.zulma.timetracking.domain.time;

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