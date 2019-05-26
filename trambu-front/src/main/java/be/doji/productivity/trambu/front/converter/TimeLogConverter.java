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
package be.doji.productivity.trambu.front.converter;

import be.doji.productivity.trambu.front.transfer.TimeLogModel;
import be.doji.productivity.trambu.infrastructure.converter.Converter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeLogConverter {

  private final ActivityDatabaseRepository activityRepository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  public TimeLogConverter(@Autowired ActivityDatabaseRepository activityDatabaseRepository) {
    this.activityRepository = activityDatabaseRepository;
  }

  public TimeLogModel parse(LogPointData data) {
    return new LogPointDataToModelConverter(data, TimeLogModel.class)
        .conversionStep(LogPointData::getStart, TimeLogModel::setStart)
        .conversionStep(LogPointData::getEnd, TimeLogModel::setEnd)
        .getConvertedData();
  }

  public LogPointData parse(TimeLogModel timeLogModel, String referenceKey) {

    LogPointData convertedData = new ModelToLogPointDataConverter(timeLogModel, LogPointData.class)
        .conversionStep(TimeLogModel::getStart, LogPointData::setStart)
        .conversionStep(TimeLogModel::getEnd, LogPointData::setEnd)
        .getConvertedData();
    Optional<ActivityData> activityData = activityRepository.findByReferenceKey(referenceKey);
    activityData.ifPresent(convertedData::setActivity);
    return convertedData;
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
