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

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.infrastructure.converter.ActivityDataConverter;
import be.doji.productivity.trambu.infrastructure.converter.Converter;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import be.doji.productivity.trambu.front.model.ActivityModel;
import be.doji.productivity.trambu.front.model.TimeLogModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityModelConverter {

  private final TimeLogConverter logConverter;
  private final ActivityDataConverter dataConverter;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  public ActivityModelConverter(@Autowired ActivityDataConverter dataConverter, @Autowired
      TimeLogConverter logConverter) {
    this.dataConverter = dataConverter;
    this.logConverter = logConverter;

  }

  public ActivityModel parse(ActivityData db) {
    ActivityModel activityModel = parse(dataConverter.parse(db));

    for (LogPointData logpoint : db.getTimelogs()) {
      activityModel.addTimeLog(logConverter.parse(logpoint));
    }

    return activityModel;
  }

  public ActivityData toDatabase(ActivityModel activityModel) {
    Activity activity = ActivityModelConverter.toDomain(activityModel);
    ActivityData activityData = dataConverter.parse(activity);
    if (activityModel.getReferenceKey() != null) {
      activityData.setReferenceKey(activityModel.getReferenceKey());
    }
    activityData.setTimelogs(parseTimelogs(activityModel.getTimelogs()));

    return activityData;
  }

  private List<LogPointData> parseTimelogs(List<TimeLogModel> timelogs) {
    List<LogPointData> logPointData = new ArrayList<>();
    for (TimeLogModel log : timelogs) {
      logPointData.add(new LogPointData(logConverter.getDateFormat().format(log.getStart()),
          log.getEnd() == null ? null :
              logConverter.getDateFormat().format(log.getEnd())));
    }
    return logPointData;
  }

  private class ActivityToActivityModelConverter extends Converter<Activity, ActivityModel> {

    ActivityToActivityModelConverter(Activity source) {
      super(source, ActivityModel.class);
    }
  }

  public ActivityModel parse(Activity activity) {

    return new ActivityToActivityModelConverter(activity)
        .conversionStep(Activity::getTitle, ActivityModel::setTitle)
        .conversionStep(this::parseDeadline, ActivityModel::setDeadline)
        .conversionStep(Activity::isCompleted, ActivityModel::setCompleted)
        .conversionStep(Activity::getTags, ActivityModel::setTags)
        .conversionStep(Activity::getProjects, ActivityModel::setProjects)
        .conversionStep(Activity::getReferenceKey, ActivityModel::setReferenceKey)
        .conversionStep(Activity::getComments, ActivityModel::setComments)
        .getConvertedData();
  }

  private Date parseDeadline(Activity activity) {
    return activity.getDeadline().map(timePoint -> timePoint.toString(
        TimePoint.EXTENDED_DATE_TIME_PATTERN))
        .map(this::mapStringToDate)
        .orElse(null);
  }

  private Date mapStringToDate(String s) {
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimePoint.EXTENDED_DATE_TIME_PATTERN);
      return simpleDateFormat.parse(s);
    } catch (ParseException e) {
      return null;
    }
  }

  private static String mapDateToString(Date d) {
    if (d == null) {
      return null;
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimePoint.EXTENDED_DATE_TIME_PATTERN);
    return simpleDateFormat.format(d);
  }


  private static class ActivityModelToActivityConverter extends Converter<ActivityModel, Activity> {

    ActivityModelToActivityConverter(ActivityModel source) {
      super(source, Activity.class);
    }
  }

  public static Activity toDomain(ActivityModel model) {
    return new ActivityModelToActivityConverter(model)
        .conversionStep(ActivityModel::getTitle, Activity::setTitle)
        .conversionStep(ActivityModel::isCompleted, Activity::setCompleted)
        .conversionStep(ActivityModelConverter::mapDeadline, Activity::setDeadline)
        .conversionStep(ActivityModel::getTags, Activity::setTags)
        .conversionStep(ActivityModel::getProjects, Activity::setProjects)
        .conversionStep(ActivityModel::getComments, Activity::setComments)
        .getConvertedData();
  }

  private static TimePoint mapDeadline(ActivityModel activityModel) {
    Date deadline = activityModel.getDeadline();
    if (deadline == null) {
      return null;
    }
    return TimePoint.fromString(mapDateToString(deadline));
  }


}
