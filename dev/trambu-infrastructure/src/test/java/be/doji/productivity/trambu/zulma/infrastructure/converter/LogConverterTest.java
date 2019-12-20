/**
 * TraMBU - an open time management tool
 *
 * Copyright (C) 2019  Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.zulma.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import be.doji.productivity.trambu.zulma.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.zulma.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.zulma.infrastructure.transfer.LogPointData;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogConverterTest {

  private static final String TIMELOG_ENTRY = "STARTTIME:2018-12-05:18:48:33.130 ENDTIME:2018-12-05:18:48:36.021";
  private static final String TIMELOG_ENTRY_NO_END = "STARTTIME:2018-12-05:18:48:33.130";
  private static final String TIMELOG_ENTRY_WITH_ACTIVITY = "ACTIVITY:[283b6271-b513-4e89-b757-10e98c9078ea] STARTTIME:2018-12-05:18:48:33.130 ENDTIME:2018-12-05:18:48:36.021";

  @Mock
  private ActivityDatabaseRepository activityRepositoryMock;

  private LogConverter logConverter;

  @Before
  public void setUp() {
    setUpMocks();
  }

  private void setUpMocks() {
    MockitoAnnotations.initMocks(this);
    ActivityData activityData = new ActivityData();
    activityData.setId(123L);
    activityData.setTitle("Some kind of title");
    when(activityRepositoryMock.findByReferenceKey("283b6271-b513-4e89-b757-10e98c9078ea"))
        .thenReturn(Optional.of(activityData));

    logConverter = new LogConverter(activityRepositoryMock);
  }

  @Test
  public void parse_startDate() {
    LogPointData parsedLog = logConverter.parse(TIMELOG_ENTRY);
    assertThat(parsedLog).isNotNull();
    assertThat(parsedLog.getStart()).isEqualTo("2018-12-05:18:48:33.130");
  }

  @Test
  public void parse_endDate() {
    LogPointData parsedLog = logConverter.parse(TIMELOG_ENTRY);
    assertThat(parsedLog).isNotNull();
    assertThat(parsedLog.getEnd()).isNotNull();
    assertThat(parsedLog.getEnd()).isEqualTo("2018-12-05:18:48:36.021");
  }

  @Test
  public void parse_endDate_noEnd() {
    LogPointData parsedLog = logConverter.parse(TIMELOG_ENTRY_NO_END);
    assertThat(parsedLog).isNotNull();
    assertThat(parsedLog.getStart()).isEqualTo("2018-12-05:18:48:33.130");
    assertThat(parsedLog.getEnd()).isEmpty();
  }

  @Test
  public void parse_activity() {
    LogPointData parsedLog = logConverter.parse(TIMELOG_ENTRY_WITH_ACTIVITY);
    assertThat(parsedLog).isNotNull();
    assertThat(parsedLog.getActivity()).isNotNull();
    assertThat(parsedLog.getActivity()).isPresent();
    assertThat(parsedLog.getActivity().get().getTitle()).isEqualTo("Some kind of title");
  }

  @Test
  public void parse_toString_withReferenceKey() {
    LogPointData logPoint = new LogPointData("2018-05-24:21:21:00.000", "2018-05-24:21:21:35.000");
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Activity with timelogs");
    activityData.setReferenceKey("283b6271-b513-4e89-b757-10e98c9078ea");
    activityData.addTimelog(logPoint);
    assertThat(logConverter.write(activityData)).isEqualTo(Arrays.asList(
        "ACTIVITY:[283b6271-b513-4e89-b757-10e98c9078ea] STARTTIME:2018-05-24:21:21:00.000 ENDTIME:2018-05-24:21:21:35.000"));
  }

  @Test
  public void parse_toString_noLogpoints() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Activity with timelogs");
    activityData.setReferenceKey("283b6271-b513-4e89-b757-10e98c9078ea");

    assertThat(logConverter.write(activityData)).isEmpty();
  }
}