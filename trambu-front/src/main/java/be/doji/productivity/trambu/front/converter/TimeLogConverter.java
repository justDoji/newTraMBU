package be.doji.productivity.trambu.front.converter;

import be.doji.productivity.trambu.front.transfer.TimeLogModel;
import be.doji.productivity.trambu.infrastructure.converter.Converter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeLogConverter {

  private final ActivityDatabaseRepository activityRepository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  public TimeLogConverter(@Autowired  ActivityDatabaseRepository activityDatabaseRepository) {
    this.activityRepository = activityDatabaseRepository;
  }

  public TimeLogModel parse(LogPointData data) {
    return new LogPointDataToModelConverter(data, TimeLogModel.class)
        .conversionStep(LogPointData::getStart, TimeLogModel::setStart)
        .conversionStep(LogPointData::getEnd, TimeLogModel::setEnd)
        .getConvertedData();
  }

  public LogPointData parse(TimeLogModel timeLogModel, String referenceKey) {
    return null;
  }

  private class LogPointDataToModelConverter extends Converter<LogPointData, TimeLogModel> {

    public LogPointDataToModelConverter(LogPointData source, Class<TimeLogModel> aClass) {
      super(source, aClass);
    }
  }

}
