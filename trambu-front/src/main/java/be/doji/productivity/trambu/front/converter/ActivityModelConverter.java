package be.doji.productivity.trambu.front.converter;

import static be.doji.productivity.trambu.domain.time.TimePoint.EXTENDED_DATE_TIME_PATTERN;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.front.elements.ActivityModel;
import be.doji.productivity.trambu.infrastructure.converter.Converter;
import org.hibernate.cfg.NotYetImplementedException;

public class ActivityModelConverter {

  private static class ActivityToActivityModelConverter extends Converter<Activity, ActivityModel> {

    ActivityToActivityModelConverter(Activity source) {
      super(source, ActivityModel.class);
    }
  }

  public static ActivityModel parse(Activity activity) {

    return new ActivityToActivityModelConverter(activity)
        .conversionStep(Activity::getTitle, ActivityModel::setTitle)
        .conversionStep(ActivityModelConverter::parseDeadline, ActivityModel::setDeadline)
        .conversionStep(Activity::isCompleted, ActivityModel::setCompleted)
        .conversionStep(Activity::getTags, ActivityModel::setTags)
        .conversionStep(Activity::getProjects, ActivityModel::setProjects)
        .getConvertedData();
  }

  private static String parseDeadline(Activity activity) {
    return activity.getDeadline().map(timePoint -> timePoint.write(EXTENDED_DATE_TIME_PATTERN))
        .orElse(null);
  }


  public static Activity toDomain(ActivityModel model) {
    throw new NotYetImplementedException("Functionality not proviced yet");
  }


}
