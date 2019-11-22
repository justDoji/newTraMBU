/**
 * TraMBU - an open time management tool
 *
 *     Copyright (C) 2019  Stijn Dejongh
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     For further information on usage, or licensing, contact the author
 *     through his github profile: https://github.com/justDoji
 */
package be.doji.productivity.trambu.timetracking.infra;

import static be.doji.productivity.trambu.timetracking.domain.Occupation.*;
import static be.doji.productivity.trambu.timetracking.domain.time.PointInTime.parse;
import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.domain.TimeServiceRule;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
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

  @Rule
  public TimeServiceRule timeRule = new TimeServiceRule();


  @Before
  public void setUp() {
    timeRule.time(of(2019, 11, 15, 12, 0));
    occupation = builder(repository, timeRule.service())
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