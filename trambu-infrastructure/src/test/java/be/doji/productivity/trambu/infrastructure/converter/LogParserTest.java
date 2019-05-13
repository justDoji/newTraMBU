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
  private static final String TIMELOG_ENTRY_WITH_ACTIVITY = "ACTIVITY:123 STARTTIME:2018-12-05:18:48:33.130 ENDTIME:2018-12-05:18:48:36.021";

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
    when(activityRepositoryMock.findById(activityData.getId()))
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
    Assertions.assertThat(parsedLog.getActivity().getTitle()).isEqualTo("Some kind of title");
  }
}