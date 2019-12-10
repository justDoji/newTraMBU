package be.doji.productivity.trambu.timetracking.infra.dto;

import be.doji.productivity.trambu.timetracking.api.Pair;
import be.doji.productivity.trambu.timetracking.api.TimeTracked;
import be.doji.productivity.trambu.timetracking.domain.Interval;
import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.time.PointInTime;
import be.doji.productivity.trambu.timetracking.domain.time.TimeService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = TimeService.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class OccupationMapper {

  @Autowired TimeService timeService;

  private static OccupationMapper INSTANCE = Mappers.getMapper(OccupationMapper.class);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "identifier", target = "correlationId")
  @Mapping(target = "id", ignore = true)
  public abstract OccupationData occupationToOccupationData(Occupation source);

  public Occupation occupationDataToOccupation(OccupationData source) {
    Occupation occupation = new Occupation(timeService);
    occupation.setName(source.getName());
    occupation.setIdentifier(source.getCorrelationId());
    return occupation;
  }

  public static OccupationMapper instance() {
    return INSTANCE;
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
    return source.dateTime();
  }

}
