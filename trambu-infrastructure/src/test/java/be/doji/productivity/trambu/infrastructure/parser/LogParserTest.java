package be.doji.productivity.trambu.infrastructure.parser;

import static org.mockito.Mockito.when;

import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

public class LogParserTest {

  private static final String TIMELOG_ENTRY = "STARTTIME:2018-12-05:18:48:33.130 ENDTIME:2018-12-05:18:48:36.021";
  private static final String TIMELOG_ENTRY_NO_END = "STARTTIME:2018-12-05:18:48:33.130";
  private static final String TIMELOG_ENTRY_WITH_ACTIVITY = "ACTIVITY:123 STARTTIME:2018-12-05:18:48:33.130 ENDTIME:2018-12-05:18:48:36.021";

  @Mock private ActivityDatabaseRepository activityRepositoryMock;

  @InjectMocks @Autowired private LogParser logParser;

  @Before
  public void setUp() {
    setUpMocks();
  }

  private void setUpMocks() {
    MockitoAnnotations.initMocks(this);
    ActivityData activityData = new ActivityData();
    activityData.setId(123L);
    activityData.setTitle("Some kind of title");
    when(activityRepositoryMock.findById(activityData.getId())).thenReturn(Optional.of(activityData));
  }

  @Test
  public void parse_startDate() {
    LogPointData parsedLog = LogParser.parse(TIMELOG_ENTRY);
    Assertions.assertThat(parsedLog).isNotNull();
    Assertions.assertThat(parsedLog.getStart()).isEqualTo("2018-12-05:18:48:33.130");
  }

  @Test
  public void parse_endDate() {
    LogPointData parsedLog = LogParser.parse(TIMELOG_ENTRY);
    Assertions.assertThat(parsedLog).isNotNull();
    Assertions.assertThat(parsedLog.getEnd()).isNotNull();
    Assertions.assertThat(parsedLog.getEnd()).isEqualTo("2018-12-05:18:48:36.021");
  }

  @Test
  public void parse_endDate_noEnd() {
    LogPointData parsedLog = LogParser.parse(TIMELOG_ENTRY_NO_END);
    Assertions.assertThat(parsedLog).isNotNull();
    Assertions.assertThat(parsedLog.getStart()).isEqualTo("2018-12-05:18:48:33.130");
    Assertions.assertThat(parsedLog.getEnd()).isEmpty();
  }

  @Test
  public void parse_activity() {
    LogPointData parsedLog = LogParser.parse(TIMELOG_ENTRY_WITH_ACTIVITY);
    Assertions.assertThat(parsedLog).isNotNull();
    Assertions.assertThat(parsedLog.getActivity()).isNotNull();
    Assertions.assertThat(parsedLog.getActivity().getTitle()).isEqualTo("Some kind of title");
  }
}