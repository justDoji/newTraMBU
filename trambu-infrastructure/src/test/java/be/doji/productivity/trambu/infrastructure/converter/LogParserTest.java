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
package be.doji.productivity.trambu.infrastructure.converter;

import static org.mockito.Mockito.when;

import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogParserTest {

  private static final String TIMELOG_ENTRY = "STARTTIME:2018-12-05:18:48:33.130 ENDTIME:2018-12-05:18:48:36.021";
  private static final String TIMELOG_ENTRY_NO_END = "STARTTIME:2018-12-05:18:48:33.130";
  private static final String TIMELOG_ENTRY_WITH_ACTIVITY = "ACTIVITY:[283b6271-b513-4e89-b757-10e98c9078ea] STARTTIME:2018-12-05:18:48:33.130 ENDTIME:2018-12-05:18:48:36.021";

  @Mock private ActivityDatabaseRepository activityRepositoryMock;

  private LogParser logParser;

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

    logParser = new LogParser(activityRepositoryMock);
  }

  @Test
  public void parse_startDate() {
    LogPointData parsedLog = logParser.parse(TIMELOG_ENTRY);
    Assertions.assertThat(parsedLog).isNotNull();
    Assertions.assertThat(parsedLog.getStart()).isEqualTo("2018-12-05:18:48:33.130");
  }

  @Test
  public void parse_endDate() {
    LogPointData parsedLog = logParser.parse(TIMELOG_ENTRY);
    Assertions.assertThat(parsedLog).isNotNull();
    Assertions.assertThat(parsedLog.getEnd()).isNotNull();
    Assertions.assertThat(parsedLog.getEnd()).isEqualTo("2018-12-05:18:48:36.021");
  }

  @Test
  public void parse_endDate_noEnd() {
    LogPointData parsedLog = logParser.parse(TIMELOG_ENTRY_NO_END);
    Assertions.assertThat(parsedLog).isNotNull();
    Assertions.assertThat(parsedLog.getStart()).isEqualTo("2018-12-05:18:48:33.130");
    Assertions.assertThat(parsedLog.getEnd()).isEmpty();
  }

  @Test
  public void parse_activity() {
    LogPointData parsedLog = logParser.parse(TIMELOG_ENTRY_WITH_ACTIVITY);
    Assertions.assertThat(parsedLog).isNotNull();
    Assertions.assertThat(parsedLog.getActivity()).isNotNull();
    Assertions.assertThat(parsedLog.getActivity()).isPresent();
    Assertions.assertThat(parsedLog.getActivity().get().getTitle()).isEqualTo("Some kind of title");
  }
}