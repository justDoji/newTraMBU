package be.doji.productivity.trambu.infrastructure.parser;

public final class PropertyRegex {

  /*Utility classes should not have a public or default constructor */
  private PropertyRegex() {
  }

  static final String REGEX_DATE = "[0-9\\-\\:\\.]*";

  static final String REGEX_TERMINATOR = "(\\s|$)";
  static final String REGEX_UUID = "([a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12})";

  static final String COMPLETED_REGEX = "^[" + PropertyIndicator.INDICATOR_DONE +
      PropertyIndicator.INDICATOR_DONE_UPPERCASE + "]";
  static final String PRIORITY_REGEX = "\\([a-zA-Z]\\)";
  static final String NAME_REGEX = "\\b[a-zA-Z]([\\w\\s\\.\\- && [^\\+]])*(\\s\\+|$|\\s\\@|\\s"
      + PropertyIndicator.INDICATOR_WARNING_PERIOD + "|\\s"
      + PropertyIndicator.INDICATOR_DEADLINE + "|\\s"
      + PropertyIndicator.INDICATOR_PARENT_ACTIVITY + "|\\s"
      + PropertyIndicator.INDICATOR_UUID + ")";

  static final String TAG_REGEX = "\\" + PropertyIndicator.INDICATOR_TAG
      + "([a-zA-Z0-9]*)" + REGEX_TERMINATOR;
  static final String PROJECT_REGEX = "\\" + PropertyIndicator.INDICATOR_PROJECT
      + "([a-zA-Z0-9]*)" + REGEX_TERMINATOR;
  static final String DUE_DATE_REGEX = PropertyIndicator.INDICATOR_DEADLINE
      + PropertyRegex.REGEX_DATE
      + REGEX_TERMINATOR;
  static final String DURATION_REGEX = "P((0-9|.)+(T)*(D|H|M|S))*";

  static final String WARNING_PERIOD_REGEX = PropertyIndicator.INDICATOR_WARNING_PERIOD
      + DURATION_REGEX + REGEX_TERMINATOR;
  static final String LOCATION_REGEX =
      PropertyIndicator.INDICATOR_LOCATION + "([a-zA-Z0-9\\s]*)" + REGEX_TERMINATOR;

  static final String SUPER_ACTIVITY_REGEX = PropertyIndicator.INDICATOR_PARENT_ACTIVITY
      + REGEX_UUID + "(\\s\\+|$|\\s\\@|\\s"
      + PropertyIndicator.INDICATOR_WARNING_PERIOD
      + "|\\s" + PropertyIndicator.INDICATOR_DEADLINE + "|\\s"
      + PropertyIndicator.INDICATOR_UUID + "|"
      + REGEX_TERMINATOR + ")";
  static final String REGEX_ID = PropertyIndicator.INDICATOR_UUID
      + REGEX_UUID + REGEX_TERMINATOR;
}
