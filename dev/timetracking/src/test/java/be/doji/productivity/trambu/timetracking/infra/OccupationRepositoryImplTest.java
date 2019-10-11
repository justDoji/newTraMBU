package be.doji.productivity.trambu.timetracking.infra;

import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
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
        .name(EXPECTED_NAME)
        .build();
  }

  @Test
  public void whenCreatingAnInterval_itIsAccessableFromTheRepository() {
    // Creation in setUp method

    Optional<Occupation> fromRepository = repository.occupationById(ROOT_IDENTIFIER);

    Assertions.assertThat(fromRepository).isPresent();
    //noinspection OptionalGetWithoutIsPresent
    Assertions.assertThat(fromRepository.get().getName()).isEqualTo(EXPECTED_NAME);
  }
}