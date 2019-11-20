package be.doji.productivity.trambu.timetracking.domain.time;

public final class TimeFormat {

  static final String BASIC_DATE_PATTERN = "dd/MM/uuuu";
  static final String BASIC_DATE_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d";
  static final String BASIC_DATE_TIME_PATTERN = "dd/MM/uuuu HH:mm:ss";
  static final String BASIC_DATE_TIME_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d";
  static final String EXTENDED_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss:SSS";
  static final String EXTENDED_DATE_TIME_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d:\\d\\d\\d";
  static final String LEGACY_DATE_TIME_PATTERN = "uuuu-MM-dd:HH:mm:ss.SSS";
  static final String LEGACY_DATE_TIME_REGEX = "\\d\\d\\d\\d-\\d\\d-\\d\\d:\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d";
}
