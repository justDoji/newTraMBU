package be.doji.productivity.trambu.infrastructure.converter;

import static be.doji.productivity.trambu.domain.time.TimePoint.LEGACY_DATE_TIME_PATTERN;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityProjectData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityTagData;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ActivityDataConverter {

  public static final DateTimeFormatter LEGACY_FORMATTER = DateTimeFormatter
      .ofPattern(LEGACY_DATE_TIME_PATTERN, Locale.FRANCE);

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
        .map(timePoint -> LEGACY_FORMATTER.format(timePoint.toLocalDateTime()))
        .orElse("");
  }
}
