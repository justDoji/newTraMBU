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
  static final String NAME_REGEX = "\\" + PropertyIndicator.INDICATOR_TITLE_START + ".*" + "\\" + PropertyIndicator.INDICATOR_TITLE_END;

}
