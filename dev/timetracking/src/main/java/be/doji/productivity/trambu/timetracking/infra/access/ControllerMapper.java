package be.doji.productivity.trambu.timetracking.infra.access;

import be.doji.productivity.trambu.timetracking.api.TimeTracked;
import be.doji.productivity.trambu.timetracking.domain.Occupation;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class ControllerMapper {

  private static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

  static {
    ConverterFactory converterFactory = mapperFactory.getConverterFactory();
    converterFactory.registerConverter(new OccupationConverter());
  }

  public static TimeTracked timeTracked(Occupation source) {
    return mapperFactory.getMapperFacade().map(source, TimeTracked.class);
  }

}
