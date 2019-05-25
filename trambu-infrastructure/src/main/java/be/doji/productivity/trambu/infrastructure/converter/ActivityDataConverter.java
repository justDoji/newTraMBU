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
package be.doji.productivity.trambu.infrastructure.converter;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityProjectData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityTagData;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ActivityDataConverter {

  private class ActivityToActivityDataConverter extends Converter<Activity, ActivityData> {

    ActivityToActivityDataConverter(Activity source) {
      super(source, ActivityData.class);
    }
  }

  public ActivityData parse(Activity activity) {
    return new ActivityToActivityDataConverter(activity)
        .conversionStep(Activity::getTitle, ActivityData::setTitle)
        .conversionStep(Activity::isCompleted, ActivityData::setCompleted)
        .conversionStep(ActivityDataConverter::parseDeadline, ActivityData::setDeadline)
        .conversionStep(ActivityDataConverter::parseTags, ActivityData::setTags)
        .conversionStep(ActivityDataConverter::parseProjects, ActivityData::setProjects)
        .conversionStep(Activity::getReferenceKey, ActivityData::setReferenceKey)
        .getConvertedData();
  }

  private static List<ActivityTagData> parseTags(Activity activity) {
    return activity.getTags().stream().map(ActivityTagData::new).collect(Collectors.toList());
  }

  private static List<ActivityProjectData> parseProjects(Activity activity) {
    return activity.getProjects().stream().map(ActivityProjectData::new)
        .collect(Collectors.toList());
  }

  private static String parseDeadline(Activity activity) {
    Optional<TimePoint> deadline = activity.getDeadline();
    return deadline
        .map(timePoint -> Property.LEGACY_FORMATTER.format(timePoint.toLocalDateTime()))
        .orElse("");
  }

  private class ActivityDataToActivityConverter extends Converter<ActivityData, Activity> {

    ActivityDataToActivityConverter(ActivityData source) {
      super(source, Activity.class);
    }
  }

  public Activity parse(ActivityData data) {
    return new ActivityDataToActivityConverter(data)
        .conversionStep(ActivityData::getTitle, Activity::setTitle)
        .conversionStep(ActivityData::getDeadline, Activity::setDeadline)
        .conversionStep(ActivityData::isCompleted, Activity::setCompleted)
        .conversionStep(ActivityDataConverter::parseProjects, Activity::setProjects)
        .conversionStep(ActivityDataConverter::parseTags, Activity::setTags)
        .getConvertedData();

  }

  private static List<String> parseProjects(ActivityData data) {
    return data.getProjects().stream().map(ActivityProjectData::getValue)
        .collect(Collectors.toList());
  }

  private static List<String> parseTags(ActivityData data) {
    return data.getTags().stream().map(ActivityTagData::getValue)
        .collect(Collectors.toList());
  }
}
