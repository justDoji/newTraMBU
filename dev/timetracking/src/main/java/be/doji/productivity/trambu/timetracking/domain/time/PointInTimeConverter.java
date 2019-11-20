package be.doji.productivity.trambu.timetracking.domain.time;

import static be.doji.productivity.trambu.timetracking.domain.time.TimeFormatSpecification.EXTENDED_DATE_TIME;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class PointInTimeConverter extends BidirectionalConverter<PointInTime, String> {

  @Override
  public String convertTo(PointInTime source, Type<String> destinationType,
      MappingContext mappingContext) {
    return EXTENDED_DATE_TIME.string(source);
  }

  @Override
  public PointInTime convertFrom(String source, Type<PointInTime> destinationType,
      MappingContext mappingContext) {
    return TimeFormatSpecification.parse(source);
  }
}
