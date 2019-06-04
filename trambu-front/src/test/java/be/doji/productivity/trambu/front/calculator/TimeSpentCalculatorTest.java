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
package be.doji.productivity.trambu.front.calculator;

import be.doji.productivity.trambu.front.model.ActivityModel;
import be.doji.productivity.trambu.front.model.TimeLogModel;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TimeSpentCalculatorTest {


  @Test
  public void hoursSpentTotal_StartAfterEnd() {
    ActivityModel model = new ActivityModel();
    TimeLogModel log = new TimeLogModel();
    Calendar startTime = new GregorianCalendar();
    startTime.set(2019, Calendar.DECEMBER, 18, 12, 0, 0);
    log.setStart(startTime.getTime());

    Calendar endTime = new GregorianCalendar();
    endTime.set(2019, Calendar.DECEMBER, 10, 12, 0, 0);
    log.setEnd(endTime.getTime());

    model.addTimeLog(log);

    String calucated = TimeSpentCalculator.hoursSpentTotal(model);
    Assertions.assertThat(calucated).isEqualTo("0.00");
  }

  @Test
  public void hoursSpentTotal_NoEnd() {
    ActivityModel model = new ActivityModel();
    TimeLogModel log = new TimeLogModel();
    Calendar startTime = Calendar.getInstance(); //today
    startTime.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY) - 2);

    log.setStart(startTime.getTime());

    model.addTimeLog(log);

    String calucated = TimeSpentCalculator.hoursSpentTotal(model);
    Assertions.assertThat(calucated).isEqualTo("2.00");
  }

}