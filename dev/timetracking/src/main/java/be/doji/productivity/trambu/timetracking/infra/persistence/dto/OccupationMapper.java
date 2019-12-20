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
package be.doji.productivity.trambu.timetracking.infra.persistence.dto;

import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.api.dto.Pair;
import be.doji.productivity.trambu.timetracking.api.dto.TimeTracked;
import be.doji.productivity.trambu.timetracking.domain.Interval;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.domain.time.PointInTime;
import be.doji.productivity.trambu.timetracking.domain.time.TimeService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class OccupationMapper {

  @Autowired
  TimeService timeService;

  @Mapping(source = "name", target = "name")
  @Mapping(source = "identifier", target = "correlationId")
  @Mapping(target = "id", ignore = true)
  public abstract OccupationData occupationToOccupationData(Occupation source);

  public Occupation occupationDataToOccupation(OccupationData source,
      OccupationRepository repository) {
    Occupation occupation = new Occupation(timeService, repository);
    occupation.setName(source.getName());
    occupation.setIdentifier(source.getCorrelationId());
    return occupation;
  }

  @Mapping(source = "name", target = "title")
  @Mapping(source = "identifier", target = "reference")
  @Mapping(source = "timeSpentInHours", target = "timeSpentInHours")
  @Mapping(source = "intervals", target = "timeEntries")
  public abstract TimeTracked occupationToTimeTracked(Occupation source);

  @Mapping(source = "start", target = "first")
  @Mapping(source = "end", target = "second")
  public abstract Pair<LocalDateTime, LocalDateTime> intervalToPair(Interval interval);

  String map(UUID source) {
    return source.toString();
  }

  LocalDateTime pointInTimeToDateTime(PointInTime source) {
    return source == null ? null : source.dateTime();
  }


}
