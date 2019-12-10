package be.doji.productivity.trambu.timetracking.infra.dto;

import be.doji.productivity.trambu.timetracking.domain.Interval;
import be.doji.productivity.trambu.timetracking.domain.time.PointInTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IntervalMapper {

  IntervalMapper INSTANCE = Mappers.getMapper(IntervalMapper.class);

  static IntervalMapper instance() {
    return INSTANCE;
  }

  @Mapping(source = "start", target = "start")
  @Mapping(source = "end", target = "end")
  @Mapping(source = "correlationId", target = "occupationId")
  Interval intervalDataToInterval(IntervalData source);

  @Mapping(source = "start", target = "start")
  @Mapping(source = "end", target = "end")
  @Mapping(source = "occupationId", target = "correlationId")
  @Mapping(target = "id", ignore = true)
  IntervalData intervalToIntervalData(Interval source);

  default String map(PointInTime source) {
    return source.toString();
  }

  default PointInTime map(String source) {
    return PointInTime.parse(source);
  }

}
