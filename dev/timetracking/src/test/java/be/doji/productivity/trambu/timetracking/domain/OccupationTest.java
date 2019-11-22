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