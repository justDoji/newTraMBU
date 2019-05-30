package be.doji.productivity.trambu.front.calculator;

import be.doji.productivity.trambu.front.transfer.ActivityModel;
import be.doji.productivity.trambu.front.transfer.TimeLogModel;
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
