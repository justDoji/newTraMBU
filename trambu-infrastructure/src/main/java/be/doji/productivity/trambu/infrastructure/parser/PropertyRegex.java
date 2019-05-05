/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
