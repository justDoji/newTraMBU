package be.doji.productivity.trambu.infrastructure.parser;

import org.apache.commons.lang3.StringUtils;

public final class DataFilePropertyRegex {

  /*Utility classes should not have a public or default constructor */
  private DataFilePropertyRegex() {
  }

  static final String REGEX_DATE = "[0-9\\-\\:\\.]*";

  static final String REGEX_TERMINATOR = "(\\s|$)";
  static final String REGEX_UUID = "([a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12})";

  static final String COMPLETED_REGEX = "^[" + DataFilePropertyIndicator.INDICATOR_DONE +
      StringUtils.upperCase(DataFilePropertyIndicator.INDICATOR_DONE) + "]";
  static final String PRIORITY_REGEX = "\\([a-zA-Z]\\)";
  static final String NAME_REGEX = "\\b[a-zA-Z]([\\w\\s\\.\\- && [^\\+]])*(\\s\\+|$|\\s\\@|\\s"
      + DataFilePropertyIndicator.INDICATOR_WARNING_PERIOD + "|\\s"
      + DataFilePropertyIndicator.INDICATOR_DEADLINE + "|\\s"
      + DataFilePropertyIndicator.INDICATOR_PARENT_ACTIVITY + "|\\s"
      + DataFilePropertyIndicator.INDICATOR_UUID + ")";

  static final String TAG_REGEX = "\\" + DataFilePropertyIndicator.INDICATOR_TAG
      + "([a-zA-Z0-9]*)" + REGEX_TERMINATOR;
  static final String PROJECT_REGEX = "\\" + DataFilePropertyIndicator.INDICATOR_PROJECT
      + "([a-zA-Z0-9]*)" + REGEX_TERMINATOR;
  static final String DUE_DATE_REGEX = DataFilePropertyIndicator.INDICATOR_DEADLINE
      + DataFilePropertyRegex.REGEX_DATE
      + REGEX_TERMINATOR;
  static final String DURATION_REGEX = "P((0-9|.)+(T)*(D|H|M|S))*";

  static final String WARNING_PERIOD_REGEX = DataFilePropertyIndicator.INDICATOR_WARNING_PERIOD
      + DURATION_REGEX + REGEX_TERMINATOR;
  static final String LOCATION_REGEX =
      DataFilePropertyIndicator.INDICATOR_LOCATION + "([a-zA-Z0-9\\s]*)" + REGEX_TERMINATOR;

  static final String SUPER_ACTIVITY_REGEX = DataFilePropertyIndicator.INDICATOR_PARENT_ACTIVITY
      + REGEX_UUID + "(\\s\\+|$|\\s\\@|\\s"
      + DataFilePropertyIndicator.INDICATOR_WARNING_PERIOD
      + "|\\s" + DataFilePropertyIndicator.INDICATOR_DEADLINE + "|\\s"
      + DataFilePropertyIndicator.INDICATOR_UUID + "|"
      + REGEX_TERMINATOR + ")";
  static final String REGEX_ID = DataFilePropertyIndicator.INDICATOR_UUID
      + REGEX_UUID + REGEX_TERMINATOR;
}
