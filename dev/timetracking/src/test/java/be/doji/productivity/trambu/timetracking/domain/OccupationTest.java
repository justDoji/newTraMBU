package be.doji.productivity.trambu.timetracking.domain;

import static be.doji.productivity.trambu.timetracking.domain.time.PointInTime.parse;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

public class OccupationTest {

  private static final String EXPECTED_NAME = "Coding TRAMBU";
  private static final UUID ROOT_IDENTIFIER = UUID.randomUUID();

  @Mock
  private OccupationRepository repository;

  @Rule
  public TimeServiceRule timeRule = new TimeServiceRule();


  @Before
  public void setUp() {
    timeRule.time(of(2019, 12, 18, 12, 0));
  }

  @Test
  public void whenCreatingAnOccupation_compositeIsFilledIn() {
    Occupation testOccupation = Occupation.builder(repository, timeRule.service())
        .rootIdentifier(ROOT_IDENTIFIER)
        .name(EXPECTED_NAME)
        .interval(parse("16/12/2018 12:00:00:000"), parse("16/12/2018 14:00:00:000"))
        .build();

    assertThat(testOccupation.getName()).isEqualTo(EXPECTED_NAME);
    assertThat(testOccupation.getIntervals().get(0).getOccupationId()).isEqualTo(ROOT_IDENTIFIER);
    assertThat(testOccupation.getTimeSpentInHours()).isEqualTo(2.0);
  }

  @Test
  public void whenBusyWithAnOccupation_timeIsTracked() {
    timeRule.time(of(2019, 12, 18, 12, 0));
    Occupation testOccupation = Occupation.builder(repository, timeRule.service())
        .rootIdentifier(ROOT_IDENTIFIER)
        .name(EXPECTED_NAME)
        .build();

    testOccupation.start();

    timeRule.time(of(2019, 12, 18, 14, 30)); //Two hours have passed
    testOccupation.stop();

    assertThat(testOccupation.getTimeSpentInHours()).isEqualTo(2.5);
  }

}