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
package be.doji.productivity.trambu.zulma.front.calculator;

import be.doji.productivity.trambu.zulma.front.model.ActivityModel;
import be.doji.productivity.trambu.zulma.front.model.TimeLogModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.function.ToDoubleFunction;
import org.apache.commons.lang3.time.DateUtils;

public final class TimeSpentCalculator {

  private TimeSpentCalculator() {
    throw new IllegalStateException("Utility class");
  }

  public static String hoursSpentTotal(ActivityModel model) {
    return hoursSpent(TimeSpentCalculator::getHourDelta, model);
  }

  public static String hoursSpentToday(ActivityModel model) {
    return hoursSpent(TimeSpentCalculator::getHoursToday, model);
  }

  public static String hoursSpent(ToDoubleFunction<TimeLogModel> mapper,
      ActivityModel model) {
    BigDecimal bigDecimal = BigDecimal.valueOf(
        model.getTimelogs().stream().mapToDouble(mapper).sum());
    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
    return bigDecimal.toString();
  }

  static double getHourDelta(TimeLogModel timeLogModel) {
    Date startCount = timeLogModel.getStart();
    Date endCount = timeLogModel.getEnd() == null ? new Date() : timeLogModel.getEnd();

    if (startCount.after(endCount)) {
      return 0;
    } else {
      long miliDelta = endCount.getTime() - startCount.getTime();
      return (double) miliDelta / (1000 * 60 * 60);
    }
  }

  static double getHoursToday(TimeLogModel timeLogModel) {

    Date startOfToday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);

    Date startCount =
        timeLogModel.getStart().before(startOfToday) ? startOfToday : timeLogModel.getStart();
    Date endCount = getEndOfTodayCount(timeLogModel);

    if (startCount.after(endCount)) {
      return 0;
    } else {
      long miliDelta = endCount.getTime() - startCount.getTime();
      return (double) miliDelta / (1000 * 60 * 60);
    }
  }

  static Date getEndOfTodayCount(TimeLogModel timeLogModel) {
    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 23);
    today.set(Calendar.MINUTE, 59);
    today.set(Calendar.SECOND, 59);
    Date endOfToday = today.getTime();

    if (timeLogModel.getEnd() == null) {
      return new Date();
    } else {
      return timeLogModel.getEnd().after(endOfToday) ? endOfToday : timeLogModel.getEnd();
    }
  }

}
