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

final class Property {


  /*Utility classes should not have a public or default constructor */
  private Property() {
    throw new UnsupportedOperationException();
  }

  final class Regex {

    /*Utility classes should not have a public or default constructor */
    private Regex() {
      throw new UnsupportedOperationException();
    }

    static final String DATE = "[0-9\\-\\:\\.]*";
    static final String ANYTHING = "(.*?)";

    static final String TERMINATOR = "(\\s|$)";
    static final String GROUP_START = "\\" + Indicator.GROUP_START;
    static final String GROUP_END = "\\" + Indicator.GROUP_END;

    static final String COMPLETED = "^[" + Indicator.DONE + Indicator.DONE_UPPERCASE + "]";

    static final String TITLE = GROUP_START
        + ANYTHING
        + GROUP_END
        + TERMINATOR;
    static final String DEADLINE = Indicator.DEADLINE
        + DATE
        + TERMINATOR;
    static final String TAG = "\\" + Indicator.TAG
        + GROUP_START
        + ANYTHING
        + GROUP_END
        + TERMINATOR;

    static final String PROJECT = "\\" + Indicator.PROJECT
        + GROUP_START
        + ANYTHING
        + GROUP_END
        + TERMINATOR;


    static final String LOGPOINT_START = Indicator.LOGPOINT_START + DATE + TERMINATOR;
    static final String LOGPOINT_END = Indicator.LOGPOINT_END + DATE + TERMINATOR;
    static final String LOGPOINT_ACTIVITY = Indicator.LOGPOINT_ACTIVITY + ANYTHING + TERMINATOR;
  }

  final class Indicator {


    /*Utility classes should not have a public or default constructor */
    private Indicator() {
      throw new UnsupportedOperationException();
    }

    static final String DONE = "x";
    static final String DONE_UPPERCASE = "X";
    static final String GROUP_START = "[";
    static final String GROUP_END = "]";
    static final String PROJECT = "+";
    static final String TAG = "@";
    static final String DEADLINE = "due:";
    static final String WARNING_PERIOD = "warningPeriod:";
    static final String UUID = "uuid:";

    static final String LOGPOINT_ACTIVITY = "ACTIVITY:";
    static final String LOGPOINT_START = "STARTTIME:";
    static final String LOGPOINT_END = "ENDTIME:";

  }
}

