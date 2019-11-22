package be.doji.productivity.trambu.timetracking.infra.access;

import static be.doji.productivity.trambu.timetracking.domain.time.PointInTime.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import be.doji.productivity.trambu.timetracking.api.Pair;
import be.doji.productivity.trambu.timetracking.api.TimeTracked;
import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.domain.TimeServiceRule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests the inner logic and application functionality from the external endpoint down. To be used
 * to test functional requirements of the system. Assumes the persistence layer is up to spec
 *
 * Scope: Endpoint + Domain logic Not In Scope: Persistence layer
 */
@RunWith(SpringRunner.class)
@WebMvcTest(OccupationController.class)
public class OccupationControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private OccupationRepository occupationRepository;

  @Rule
  public TimeServiceRule timeRule = new TimeServiceRule();

  private static final UUID REFERENCE = UUID.randomUUID();
  private static final UUID REFERENCE_NOT_FOUND = UUID.randomUUID();
  private Occupation occupation;

  private JacksonTester<TimeTracked> jsonOccupation;

  @Before
  public void setUp() {
    JacksonTester.initFields(this, geObjMapper());
    when(occupationRepository.occupationById(REFERENCE_NOT_FOUND)).thenReturn(Optional.empty());

    occupation = Occupation.builder(occupationRepository, timeRule.service())
        .name("Coding a layered application")
        .rootIdentifier(REFERENCE)
        .interval(
            parse("21/11/2019 18:00:00"),
            parse("21/11/2019 21:00:00"))
        .build();
  }

  @Test
  public void canRetrieveData_WhenOccupationReferenceIsKnown() throws Exception {
    when(occupationRepository.occupationById(REFERENCE)).thenReturn(Optional.of(occupation));

    //when
    MockHttpServletResponse result = mvc.perform(
        get("/timespent/" + REFERENCE).accept(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    //then
    assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
    TimeTracked parsedData = jsonOccupation
        .parseObject(result.getContentAsByteArray());
    assertThat(parsedData.reference).isEqualTo(REFERENCE.toString());
    assertThat(parsedData.timeSpentInHours).isEqualTo(3.0);
    assertThat(parsedData.timeEntries).contains(
        Pair.of(parse("21/11/2019 18:00:00").localDateTime(),
            parse("21/11/2019 21:00:00").localDateTime())
    );
  }

  public ObjectMapper geObjMapper(){
    return new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }

}