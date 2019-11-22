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
