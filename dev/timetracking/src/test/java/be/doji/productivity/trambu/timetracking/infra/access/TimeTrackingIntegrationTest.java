package be.doji.productivity.trambu.timetracking.infra.access;

import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.timetracking.api.dto.TimeTracked;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.domain.TimeServiceRule;
import be.doji.productivity.trambu.timetracking.infra.TimetrackingApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(TrackingController.class)
@ContextConfiguration(classes = TimetrackingApplication.class)
public class TimeTrackingIntegrationTest {

  private static final LocalDateTime DECEMER_12_NOON = of(2019, 12, 12, 12, 0);
  private static final LocalDateTime DECEMBER_10_NOON = of(2019, 12, 10, 12, 0);

  @Autowired
  private MockMvc mvc;

  @Rule
  public TimeServiceRule timeRule = new TimeServiceRule();

  private static final UUID REFERENCE = UUID.randomUUID();
  private static final String TITLE = "Occupation to be created";

  @Autowired
  private OccupationRepository repository;

  private TestFlow flow;

  @Before
  public void setUp() {
    JacksonTester.initFields(this, geObjMapper());
    this.flow = new TestFlow(mvc);
    repository.clear();
  }

  @Test
  public void fullOccupationFlowWorks() throws Exception {

    MockHttpServletResponse result = flow.whenCallingCreateOccupation(TITLE, REFERENCE);
    assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());

    MockHttpServletResponse resultStarted = flow
        .whenCallingTrackingStartedForReference(REFERENCE, DECEMBER_10_NOON);
    assertThat(resultStarted.getStatus()).isEqualTo(HttpStatus.OK.value());

    MockHttpServletResponse resultStopped = flow
        .whenCallingTrackingStoppedForReference(REFERENCE, DECEMER_12_NOON);
    assertThat(resultStopped.getStatus()).isEqualTo(HttpStatus.OK.value());

    MockHttpServletResponse resultTracked = flow.whenCallingTimespentForReference(REFERENCE);
    assertThat(resultTracked.getStatus()).isEqualTo(HttpStatus.OK.value());

    //then
    TimeTracked parsedData = geObjMapper()
        .readValue(resultTracked.getContentAsByteArray(), TimeTracked.class);
    assertThat(parsedData.getReference()).isEqualTo(REFERENCE.toString());
    assertThat(parsedData.getTimeSpentInHours()).isEqualTo(48.0);
    assertThat(parsedData.getTimeEntries()).hasSize(1);
  }

  public ObjectMapper geObjMapper() {
    return new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }

}
