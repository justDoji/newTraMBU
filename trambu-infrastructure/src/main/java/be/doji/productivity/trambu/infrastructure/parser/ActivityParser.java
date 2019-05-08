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

import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import org.apache.commons.lang3.StringUtils;

public final class ActivityParser {

  private static final String DEFAULT_TITLE = "Unnamed activity";

  /* Utility classes should not have a public or default constructor */
  private ActivityParser() {
  }

  public static ActivityData parse(String line) {
    if (StringUtils.isBlank(line)) {
      throw new IllegalArgumentException(
          "Failure during parsing: empty String or null value not allowed");
    }
    ActivityData activity = new ActivityData();

    activity.setCompleted(ParserUtils.matches(PropertyRegex.COMPLETED_REGEX, line));
    activity
        .setTitle(
            stripIndicators(
                ParserUtils.findFirstMatch(PropertyRegex.NAME_REGEX, line).orElse(DEFAULT_TITLE)));

    return activity;
  }

  private static String stripIndicators(String match) {
    String strippedMatch = match
        .replaceFirst(regexEscape(PropertyIndicator.INDICATOR_TITLE_START), "");
    strippedMatch = strippedMatch
        .replaceAll(regexEscape(PropertyIndicator.INDICATOR_TITLE_END) + "$", "");

    return strippedMatch;
  }

  private static String regexEscape(String toEscape) {
    return "\\" + toEscape;
  }
}
