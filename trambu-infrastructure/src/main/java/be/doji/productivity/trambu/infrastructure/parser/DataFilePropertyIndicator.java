package be.doji.productivity.trambu.infrastructure.parser;

public final class DataFilePropertyIndicator {

  /*Utility classes should not have a public or default constructor */
  private DataFilePropertyIndicator() {}

  public static final String INDICATOR_DONE = "x";
  public static final String INDICATOR_PROJECT = "+";
  public static final String INDICATOR_TAG = "@";
  public static final String INDICATOR_DEADLINE = "due:";
  public static final String INDICATOR_WARNING_PERIOD = "warningPeriod:";
  public static final String INDICATOR_PARENT_ACTIVITY = "super:";
  public static final String INDICATOR_UUID = "uuid:";
  public static final String INDICATOR_LOCATION = "loc:";

  public static final String INDICATOR_LOG_START = "LOG_START";
  public static final String INDICATOR_LOG_END = "LOG_END";
  public static final String INDICATOR_LOGPOINT_START = "STARTTIME:";
  public static final String INDICATOR_LOGPOINT_END = "ENDTIME:";

}
