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
package be.doji.productivity.trambu.timetracking.infra.access;

import static java.util.stream.Collectors.toList;

import be.doji.productivity.trambu.timetracking.api.Pair;
import be.doji.productivity.trambu.timetracking.api.TimeTracked;
import be.doji.productivity.trambu.timetracking.domain.Interval;
import be.doji.productivity.trambu.timetracking.domain.Occupation;
import java.time.LocalDateTime;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class OccupationConverter extends
    CustomConverter<Occupation, TimeTracked> {


  @Override
  public TimeTracked convert(Occupation source,
      Type<? extends TimeTracked> destinationType, MappingContext mappingContext) {
    return TimeTracked.builder()
        .title(source.getName())
        .reference(source.getIdentifier().toString())
        .timeSpentInHours(source.getTimeSpentInHours())
        .timeEntries(
            source.getIntervals().stream()
                .map(this::intervalToPair)
                .collect(toList())
        )
        .build();
  }

  private Pair<LocalDateTime, LocalDateTime> intervalToPair(Interval interval) {
    return Pair
        .of(interval.getStart().dateTime(), interval.getEnd().dateTime());
  }
}
