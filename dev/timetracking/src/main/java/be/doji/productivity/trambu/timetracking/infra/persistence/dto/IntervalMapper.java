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

import be.doji.productivity.trambu.timetracking.domain.Interval;
import be.doji.productivity.trambu.kernel.time.PointInTime;
import be.doji.productivity.trambu.timetracking.domain.time.TimeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class IntervalMapper {

  @Autowired
  TimeService timeService;

  public Interval intervalDataToInterval(IntervalData source) {
    Interval interval = new Interval(source.getCorrelationId(), timeService);
    interval.setStart(map(source.getStart()));
    interval.setEnd(map(source.getEnd()));
    interval.setIntervalId(source.getIntervalId());
    return interval;
  }

  @Mapping(source = "start", target = "start")
  @Mapping(source = "end", target = "end")
  @Mapping(source = "occupationId", target = "correlationId")
  @Mapping(source = "intervalId", target = "intervalId")
  public abstract IntervalData intervalToIntervalData(Interval source);

  String map(PointInTime source) {
    return source == null ? null : source.toString();
  }

  PointInTime map(String source) {
    return source == null ? null : PointInTime.parse(source);
  }

}
