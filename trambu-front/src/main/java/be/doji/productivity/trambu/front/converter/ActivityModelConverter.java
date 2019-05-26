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

import static be.doji.productivity.trambu.domain.time.TimePoint.EXTENDED_DATE_TIME_PATTERN;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.front.transfer.ActivityModel;
import be.doji.productivity.trambu.infrastructure.converter.ActivityDataConverter;
import be.doji.productivity.trambu.infrastructure.converter.Converter;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityModelConverter {

  private ActivityDataConverter dataConverter;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  public ActivityModelConverter(@Autowired ActivityDataConverter dataConverter) {
    this.dataConverter = dataConverter;
  }

  public ActivityModel parse(ActivityData db) {
    ActivityModel activityModel = parse(dataConverter.parse(db));
    activityModel.setReferenceKey(db.getReferenceKey());
    return activityModel;
  }

  public ActivityData toDatabase(ActivityModel activityModel) {
    Activity activity = ActivityModelConverter.toDomain(activityModel);
    ActivityData activityData = dataConverter.parse(activity);
    if (activityModel.getDataBaseId() != null) {
      activityData.setReferenceKey(activityModel.getReferenceKey());
    }
    return activityData;
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
        .getConvertedData();
  }

  private Date parseDeadline(Activity activity) {
    return activity.getDeadline().map(timePoint -> timePoint.write(EXTENDED_DATE_TIME_PATTERN))
        .map(this::mapStringToDate)
        .orElse(null);
  }

  private Date mapStringToDate(String s) {
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(EXTENDED_DATE_TIME_PATTERN);
      return simpleDateFormat.parse(s);
    } catch (ParseException e) {
      return null;
    }
  }

  private static String mapDateToString(Date d) {
    if (d == null) {
      return null;
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(EXTENDED_DATE_TIME_PATTERN);
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
