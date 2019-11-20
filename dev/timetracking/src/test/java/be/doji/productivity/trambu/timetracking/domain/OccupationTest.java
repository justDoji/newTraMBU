package be.doji.productivity.trambu.timetracking.domain;

import static be.doji.productivity.trambu.timetracking.domain.PointInTime.fromString;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;
import java.util.UUID;
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

  @Mock
  private TimeService timeService;

  @Mock
  private Clock mockClock;

  @Before
  public void setUp() {
    setClock(of(2019, 12, 18, 12, 0));

    Services.setTimeService(timeService);
  }

  @Test
  public void whenCreatingAnOccupation_compositeIsFilledIn() {
    Occupation testOccupation = Occupation.builder(repository)
        .rootIdentifier(ROOT_IDENTIFIER)
        .name(EXPECTED_NAME)
        .interval(fromString("16/12/2018 12:00:00:000"), fromString("16/12/2018 14:00:00:000"))
        .build();

    assertThat(testOccupation.getName()).isEqualTo(EXPECTED_NAME);
    assertThat(testOccupation.getIntervals().get(0).getOccupationId()).isEqualTo(ROOT_IDENTIFIER);
    assertThat(testOccupation.getTimeSpentInHours()).isEqualTo(2.0);
  }

  @Test
  public void whenBusyWithAnOccupation_timeIsTracked() {
    setClock(of(2019, 12, 18, 12, 0));
    Occupation testOccupation = Occupation.builder(repository)
        .rootIdentifier(ROOT_IDENTIFIER)
        .name(EXPECTED_NAME)
        .build();

    testOccupation.start();

    setClock(of(2019, 12, 18, 14, 30)); //Two hours have passed
    testOccupation.stop();

    assertThat(testOccupation.getTimeSpentInHours()).isEqualTo(2.5);
  }

  private void setClock(LocalDateTime timeToSet) {
    Instant instantToSet = timeToSet.toInstant(ZoneOffset.of("+01:00"));
    PointInTime pointInTime = new PointInTime(timeToSet);

    when(mockClock.instant()).thenReturn(instantToSet);
    when(mockClock.getZone()).thenReturn(ZoneId.of(TimeZone.getDefault().getID()));
    when(timeService.getSharedClock()).thenReturn(mockClock);
    when(timeService.now()).thenReturn(pointInTime);
  }
}