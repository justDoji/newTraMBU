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
package be.doji.productivity.trambu.front.converter;

import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.front.model.TimeLogModel;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeLogConverterTest {

  private SimpleDateFormat dateFormat;
  private TimeLogConverter converter;

  @Mock
  private ActivityDatabaseRepository activityRepositoryMock;

  @Before
  public void setUp() {
    converter = new TimeLogConverter(activityRepositoryMock);
    this.dateFormat = new SimpleDateFormat(TimeLogConverter.LOG_DATE_PATTERN);
  }


  @Test
  public void parse_dbToModel() throws ParseException {
    LogPointData logPointData = new LogPointData("2018-05-24:21:21:00.000",
        "2018-05-24:21:21:35.000");
    TimeLogModel parsed = converter.parse(logPointData);
    assertThat(parsed).isNotNull();
    assertThat(parsed.getStart()).isEqualTo(dateFormat.parse("2018-05-24:21:21:00.000"));
    assertThat(parsed.getEnd()).isEqualTo(dateFormat.parse("2018-05-24:21:21:35.000"));
  }

  @Test
  public void parse_modelToDb() throws ParseException {
    ActivityData activity = new ActivityData();
    activity.setReferenceKey("283b6271-b513-4e89-b757-10e98c9078ea");
    activity.setTitle("Reference title");
    Mockito.when(activityRepositoryMock.findByReferenceKey("283b6271-b513-4e89-b757-10e98c9078ea"))
        .thenReturn(Optional.of(activity));

    TimeLogModel timeLogModel = new TimeLogModel();
    timeLogModel.setStart(dateFormat.parse("2018-05-24:21:21:00.000"));
    timeLogModel.setEnd(dateFormat.parse("2018-05-24:21:21:35.000"));

    LogPointData data = converter.parse(timeLogModel, "283b6271-b513-4e89-b757-10e98c9078ea");
    assertThat(data).isNotNull();
    assertThat(data.getStart()).isEqualTo("2018-05-24:21:21:00.000");
    assertThat(data.getEnd()).isEqualTo("2018-05-24:21:21:35.000");
    assertThat(data.getActivity()).isPresent();
    assertThat(data.getActivity().get().getTitle()).isEqualTo("Reference title");
  }

}