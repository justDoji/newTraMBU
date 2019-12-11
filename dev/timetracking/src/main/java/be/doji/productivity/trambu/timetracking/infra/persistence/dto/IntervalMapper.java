package be.doji.productivity.trambu.timetracking.infra.persistence.dto;

import be.doji.productivity.trambu.timetracking.domain.Interval;
import be.doji.productivity.trambu.timetracking.domain.time.PointInTime;
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
