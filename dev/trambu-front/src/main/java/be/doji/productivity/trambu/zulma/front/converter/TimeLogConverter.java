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
package be.doji.productivity.trambu.zulma.front.converter;

import be.doji.productivity.trambu.zulma.front.model.TimeLogModel;
import be.doji.productivity.trambu.zulma.infrastructure.converter.Converter;
import be.doji.productivity.trambu.zulma.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.zulma.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.zulma.infrastructure.transfer.LogPointData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeLogConverter {

  public static final String LOG_DATE_PATTERN = "yyyy-MM-dd:HH:mm:ss.SSS";
  private final SimpleDateFormat dateFormat;

  private final ActivityDatabaseRepository activityRepository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  public TimeLogConverter(@Autowired ActivityDatabaseRepository activityDatabaseRepository) {
    this.activityRepository = activityDatabaseRepository;
    this.dateFormat = new SimpleDateFormat(LOG_DATE_PATTERN);
  }

  public SimpleDateFormat getDateFormat() {
    return this.dateFormat;
  }

  public TimeLogModel parse(LogPointData data) {
    return new LogPointDataToModelConverter(data, TimeLogModel.class)
        .conversionStep(this::mapStart, TimeLogModel::setStart)
        .conversionStep(this::mapEnd, TimeLogModel::setEnd)
        .getConvertedData();
  }

  private Date mapStart(LogPointData data) {
    try {
      return dateFormat.parse(data.getStart());
    } catch (ParseException e) {
      return null;
    }
  }

  private Date mapEnd(LogPointData data) {
    try {
      return dateFormat.parse(data.getEnd());
    } catch (ParseException e) {
      return null;
    }
  }

  public LogPointData parse(TimeLogModel timeLogModel, String referenceKey) {

    LogPointData convertedData = new ModelToLogPointDataConverter(timeLogModel, LogPointData.class)
        .conversionStep(this::mapStart, LogPointData::setStart)
        .conversionStep(this::mapEnd, LogPointData::setEnd)
        .getConvertedData();
    Optional<ActivityData> activityData = activityRepository.findByReferenceKey(referenceKey);
    activityData.ifPresent(convertedData::setActivity);
    return convertedData;
  }

  private String mapStart(TimeLogModel timeLogModel) {
    return dateFormat.format(timeLogModel.getStart());
  }

  private String mapEnd(TimeLogModel timeLogModel) {
    return dateFormat.format(timeLogModel.getEnd());
  }

  private class LogPointDataToModelConverter extends Converter<LogPointData, TimeLogModel> {

    public LogPointDataToModelConverter(LogPointData source, Class<TimeLogModel> aClass) {
      super(source, aClass);
    }
  }

  private class ModelToLogPointDataConverter extends Converter<TimeLogModel, LogPointData> {

    public ModelToLogPointDataConverter(TimeLogModel source, Class<LogPointData> aClass) {
      super(source, aClass);
    }
  }

}
