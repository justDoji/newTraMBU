package be.doji.productivity.trambu.timetracking.domain;

import static be.doji.productivity.trambu.timetracking.domain.PointInTime.TimeConverter.fromDate;
import static be.doji.productivity.trambu.timetracking.domain.PointInTime.TimeConverter.fromDateTime;

import be.doji.productivity.trambu.kernel.annotations.ValueObject;
import com.google.re2j.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

@ValueObject
public final class PointInTime {

  private final LocalDateTime internalDateTime;

  PointInTime(LocalDateTime temporal) {
    this.internalDateTime = temporal;
  }

  public static PointInTime fromString(String toParse) {
    return new PointInTime(
        Factory.fromString(toParse)
            .orElseThrow(() -> couldNotParseException(toParse))
    );
  }

  private static IllegalArgumentException couldNotParseException(String toParse) {
    return new IllegalArgumentException(String.format(
        "Could not parse given Date string: No matching parsers found for string [ %s] ",
        toParse));
  }


  public LocalDateTime localDateTime() {
    return this.internalDateTime;
  }

  private static class Factory {
    //TODO: use mapper here? Rename converter to Specification

    static final String BASIC_DATE_PATTERN = "dd/MM/uuuu";
    static final String BASIC_DATE_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d";

    static final String BASIC_DATE_TIME_PATTERN = "dd/MM/uuuu HH:mm:ss";
    static final String BASIC_DATE_TIME_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d";

    static final String EXTENDED_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss:SSS";
    static final String EXTENDED_DATE_TIME_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d:\\d\\d\\d";

    static final String LEGACY_DATE_TIME_PATTERN = "uuuu-MM-dd:HH:mm:ss.SSS";
    static final String LEGACY_DATE_TIME_REGEX = "\\d\\d\\d\\d-\\d\\d-\\d\\d:\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d";

    static List<TimeConverter> converters = new ArrayList<>();

    private static TimeConverter defaultConverter;


    static {
      converters.add(fromDate(BASIC_DATE_REGEX, BASIC_DATE_PATTERN));
      converters.add(fromDateTime(BASIC_DATE_TIME_REGEX, BASIC_DATE_TIME_PATTERN));
      defaultConverter = fromDateTime(EXTENDED_DATE_TIME_REGEX, EXTENDED_DATE_TIME_PATTERN);
      converters.add(defaultConverter);
      converters.add(fromDateTime(LEGACY_DATE_TIME_REGEX, LEGACY_DATE_TIME_PATTERN));
    }


    public static Optional<LocalDateTime> fromString(String toParse) {
      for (TimeConverter converter : converters) {
        if (converter.isApplicable(toParse)) {
          return Optional.of(converter.getTemporal(toParse));
        }
      }
      return Optional.empty();
    }

    public static String toString(PointInTime pointInTime) {
      return defaultConverter.toString(pointInTime);
    }
  }

  static class TimeConverter {

    private final boolean withTime;
    private Pattern regex;
    private DateTimeFormatter formatter;

    public static TimeConverter fromDate(String regex, String formatter) {
      return new TimeConverter(regex, formatter, false);
    }

    public static TimeConverter fromDateTime(String regex, String formatter) {
      return new TimeConverter(regex, formatter, true);
    }

    private TimeConverter(String regex, String formatString, boolean includeTime) {
      this.regex = Pattern.compile(regex);
      this.formatter = DateTimeFormatter.ofPattern(formatString, Locale.FRANCE);
      this.withTime = includeTime;
    }

    private LocalDateTime getTemporal(String dateTimeString) {
      return withTime ? LocalDateTime.parse(dateTimeString, formatter)
          : LocalDate.parse(dateTimeString, formatter).atStartOfDay();
    }

    private String toString(PointInTime pointInTime) {
      return formatter.format(pointInTime.localDateTime());
    }

    public boolean isApplicable(String dateTimeString) {
      return regex.matches(dateTimeString);
    }
  }

  public static class PointInTimeConverter extends BidirectionalConverter<PointInTime, String> {

    @Override
    public String convertTo(PointInTime source, Type<String> destinationType,
        MappingContext mappingContext) {
      return Factory.toString(source);
    }

    @Override
    public PointInTime convertFrom(String source, Type<PointInTime> destinationType,
        MappingContext mappingContext) {
      return PointInTime.fromString(source);
    }
  }

}
