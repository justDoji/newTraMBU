package be.doji.productivity.trambu.infrastructure.parser;

public final class PropertyIndicator {

  /*Utility classes should not have a public or default constructor */
  private PropertyIndicator() {
  }

  static final String INDICATOR_DONE = "x";
  static final String INDICATOR_DONE_UPPERCASE = "X";
  static final String INDICATOR_PROJECT = "+";
  static final String INDICATOR_TAG = "@";
  static final String INDICATOR_DEADLINE = "due:";
  static final String INDICATOR_WARNING_PERIOD = "warningPeriod:";
  static final String INDICATOR_PARENT_ACTIVITY = "super:";
  static final String INDICATOR_UUID = "uuid:";
  static final String INDICATOR_LOCATION = "loc:";

  static final String INDICATOR_LOG_START = "LOG_START";
  static final String INDICATOR_LOG_END = "LOG_END";
  static final String INDICATOR_LOGPOINT_START = "STARTTIME:";
  static final String INDICATOR_LOGPOINT_END = "ENDTIME:";

}
