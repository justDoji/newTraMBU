package be.doji.productivity.trambu.timetracking.domain;

import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OccupationTest {

  private static final String EXPECTED_NAME = "Coding TRAMBU";
  private static final UUID ROOT_IDENTIFIER = UUID.randomUUID();

  @Mock
  private OccupationRepository repository;

  private Occupation testOccupation;

  @Before
  public void setUp() throws Exception {
    testOccupation = Occupation.builder(repository)
        .rootIdentifier(ROOT_IDENTIFIER)
        .name(EXPECTED_NAME)
        .interval(PointInTime.fromString("16/12/2018 12:00:00:000"),
            PointInTime.fromString("16/12/2018 14:00:00:000"))
        .build();
  }

  @Test
  public void whenCreatingAnOccupation_compositeIsFilledIn() {
    // Creation in setUp method

    Assertions.assertThat(testOccupation.getName()).isEqualTo(EXPECTED_NAME);
    Assertions.assertThat(testOccupation.getIntervals().get(0).getOccupationId()).isEqualTo(ROOT_IDENTIFIER);
    Assertions.assertThat(testOccupation.getTimeSpentInHours()).isEqualTo(2.0);
  }

}