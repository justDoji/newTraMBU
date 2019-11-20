package be.doji.productivity.trambu.timetracking.infra;

import static be.doji.productivity.trambu.timetracking.domain.time.PointInTime.parse;
import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OccupationRepositoryImplTest {

  public static final String EXPECTED_NAME = "Coding TRAMBU";
  private static final UUID ROOT_IDENTIFIER = UUID.randomUUID();

  private Occupation occupation;
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")

  @Autowired
  public OccupationRepository repository;

  @Before
  public void setUp() {
    occupation = Occupation.builder(repository)
        .rootIdentifier(ROOT_IDENTIFIER)
        .interval(
            parse("05/05/2019 12:00:00"),
            parse("06/05/2019 12:00:00")
        )
        .name(EXPECTED_NAME)
        .build();
  }

  @Test
  public void whenCreatingAnInterval_itIsAccessableFromTheRepository() {
    // Creation in setUp method

    Optional<Occupation> fromRepository = repository.occupationById(ROOT_IDENTIFIER);

    assertThat(fromRepository).isPresent();
    //noinspection OptionalGetWithoutIsPresent
    assertThat(fromRepository.get().getName()).isEqualTo(EXPECTED_NAME);
    assertThat(fromRepository.get().getTimeSpentInHours()).isEqualTo(24.0);
  }
}