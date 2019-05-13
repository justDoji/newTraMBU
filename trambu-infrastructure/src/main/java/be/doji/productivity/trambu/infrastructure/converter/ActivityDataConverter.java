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
