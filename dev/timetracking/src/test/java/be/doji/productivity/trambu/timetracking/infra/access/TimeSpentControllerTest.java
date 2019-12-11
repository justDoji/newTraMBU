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
package be.doji.productivity.trambu.timetracking.infra.access;

import static be.doji.productivity.trambu.timetracking.domain.time.PointInTime.parse;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import be.doji.productivity.trambu.timetracking.api.Pair;
import be.doji.productivity.trambu.timetracking.api.TimeTracked;
import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.domain.TimeServiceRule;
import be.doji.productivity.trambu.timetracking.domain.time.PointInTime;
import be.doji.productivity.trambu.timetracking.infra.TimetrackingApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.time.LocalDateTime;
import java.util.List;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests the inner logic and application functionality from the external endpoint down. To be used
 * to test functional requirements of the system. Assumes the persistence layer is up to spec
 *
 * Scope: Endpoint + Domain logic Not In Scope: Persistence layer
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TimeSpentController.class)
@ContextConfiguration(classes = TimetrackingApplication.class)
public class TimeSpentControllerTest {


  @Autowired
  private MockMvc mvc;

  @MockBean
  private OccupationRepository occupationRepository;

  @Rule
  public TimeServiceRule timeRule = new TimeServiceRule();

  private static final UUID REFERENCE = UUID.randomUUID();
  private static final UUID REFERENCE_NOT_FOUND = UUID.randomUUID();

  private Occupation occupation;
  private static final PointInTime START_TIME = parse("21/11/2019 18:00:00");
  private static final PointInTime END_TIME = parse("21/11/2019 21:00:00");
  private static final double EXPECTED_HOURS_SPENT = 3.0;
  public static final String EXPECTED_TITLE = "Coding a layered application";

  private JacksonTester<TimeTracked> jsonOccupation;

  @Before
  public void setUp() {
    JacksonTester.initFields(this, geObjMapper());
    when(occupationRepository.occupationById(REFERENCE_NOT_FOUND)).thenReturn(Optional.empty());

    occupation = createOccupation(EXPECTED_TITLE, REFERENCE, START_TIME, END_TIME);
  }

  @Test
  public void retrieveTimeTracked_canRetrieve_WhenOccupationReferenceIsKnown() throws Exception {
    given(occupationRepository.occupationById(REFERENCE)).willReturn(Optional.of(occupation));

    //when
    MockHttpServletResponse result =
        whenCallingTimespentForReference(REFERENCE);

    //then
    assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
    TimeTracked parsedData = jsonOccupation
        .parseObject(result.getContentAsByteArray());
    assertTimeTrackedContains(parsedData, EXPECTED_TITLE, REFERENCE, EXPECTED_HOURS_SPENT,
        singletonList(Pair.of(START_TIME.dateTime(), END_TIME.dateTime())));
  }

  @Test
  public void retrieveTimeTracked_throwsError_whenReferenceUnknown() throws Exception {
    given(occupationRepository.occupationById(REFERENCE_NOT_FOUND)).willReturn(Optional.empty());

    //when
    MockHttpServletResponse result =
        whenCallingTimespentForReference(REFERENCE_NOT_FOUND);

    //then
    assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  public MockHttpServletResponse whenCallingTimespentForReference(UUID reference) throws Exception {
    return mvc.perform(
        get("/timespent")
            .param("reference", reference.toString())
            .accept(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();
  }

  public Occupation createOccupation(String title, UUID reference, PointInTime startTime,
      PointInTime endTime) {
    return Occupation.builder(occupationRepository, timeRule.service())
        .name(title)
        .rootIdentifier(reference)
        .interval(
            startTime,
            endTime)
        .build();
  }

  public void assertTimeTrackedContains(TimeTracked parsedData, String expectedTitle,
      UUID reference, double expectedHoursSpent,
      List<Pair<LocalDateTime, LocalDateTime>> intervals) {
    assertThat(parsedData.title).isEqualTo(expectedTitle);
    assertThat(parsedData.reference).isEqualTo(reference.toString());
    assertThat(parsedData.timeSpentInHours).isEqualTo(expectedHoursSpent);
    assertThat(parsedData.timeEntries).containsAll(intervals);
  }

  public ObjectMapper geObjMapper() {
    return new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }

}