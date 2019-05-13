/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

public final class ActivityDataConverter {

  /* Utility classes should not have a public or default constructor */
  private ActivityDataConverter() {throw new UnsupportedOperationException();}

  private static class ActivityToActivityDataConverter extends Converter<Activity, ActivityData> {

    ActivityToActivityDataConverter(Activity source) {
      super(source, ActivityData.class);
    }
  }

  public static ActivityData parse(Activity activity) {
    return new ActivityToActivityDataConverter(activity)
        .conversionStep(Activity::getTitle, ActivityData::setTitle)
        .conversionStep(Activity::isCompleted, ActivityData::setCompleted)
        .conversionStep(ActivityDataConverter::parseDeadline, ActivityData::setDeadline)
        .conversionStep(ActivityDataConverter::parseTags, ActivityData::setTags)
        .conversionStep(ActivityDataConverter::parseProjects, ActivityData::setProjects)
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

  private static class ActivityDataToActivityConverter extends Converter<ActivityData, Activity> {

    ActivityDataToActivityConverter(ActivityData source) {
      super(source, Activity.class);
    }
  }

  public static Activity parse(ActivityData data) {
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
