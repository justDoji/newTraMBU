/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package be.doji.productivity.trambu.infrastructure.converter;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.infrastructure.converter.Property.Indicator;
import be.doji.productivity.trambu.infrastructure.converter.Property.Regex;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public final class ActivityConverter {

  private static final String DEFAULT_TITLE = "Unnamed activity";

  /* Utility classes should not have a public or default constructor */
  private ActivityConverter() {throw new UnsupportedOperationException();}

  private static class StringToActivityConverter extends Converter<String, Activity> {

    StringToActivityConverter(String source) {
      super(source, Activity.class);
    }
  }

  public static Activity parse(String line) {
    if (StringUtils.isBlank(line)) {
      throw new IllegalArgumentException(
          "Failure during parsing: empty String or null value not allowed");
    }

    return new StringToActivityConverter(line)
        .conversionStep(ActivityConverter::parseCompleted, Activity::setCompleted)
        .conversionStep(ActivityConverter::parseTitle, Activity::setTitle)
        .conversionStep(ActivityConverter::parseDeadline, Activity::setDeadline)
        .conversionStep(ActivityConverter::parseTags, Activity::setTags)
        .conversionStep(ActivityConverter::parseProjects, Activity::setProjects)
        .getConvertedData();
  }

  private static boolean parseCompleted(String line) {
    return ParserUtils.matches(Regex.COMPLETED, line);
  }

  private static String parseTitle(String line) {
    return stripIndicators(
        ParserUtils.findFirstMatch(Regex.TITLE, line).orElse(DEFAULT_TITLE)).trim();
  }

  private static String parseDeadline(String line) {
    return findAndStripIndicators(Indicator.DEADLINE, Regex.DEADLINE, line);
  }

  private static List<String> parseTags(String line) {
    List<String> tagMatches = ParserUtils.findAllMatches(Regex.TAG, line);

    return tagMatches.stream()
        .map(ActivityConverter::stripIndicators)
        .map(tag -> ParserUtils.replaceFirst(Indicator.TAG, tag, ""))
        .map(String::trim)
        .collect(Collectors.toList());
  }

  private static List<String> parseProjects(String line) {
    List<String> projectMatches = ParserUtils.findAllMatches(Regex.PROJECT, line);

    return projectMatches.stream()
        .map(ActivityConverter::stripIndicators)
        .map(project -> ParserUtils.replaceFirst(regexEscape(Indicator.PROJECT), project, ""))
        .map(String::trim)
        .collect(Collectors.toList());
  }


  public String toString(ActivityData activityToWrite) {

    return "";
  }

  /* UTILITY METHODS */

  private static String stripIndicators(String toStrip) {
    String strippedMatch = ParserUtils
        .replaceFirst(regexEscape(Indicator.GROUP_START), toStrip, "");
    return ParserUtils.replaceLast(regexEscape(Indicator.GROUP_END), strippedMatch, "");
  }

  private static String findAndStripIndicators(String escapedIndicator, String regex,
      String line) {
    return ParserUtils
        .findFirstMatch(regex, line)
        .map(s -> stripIndicators(s, escapedIndicator).trim())
        .orElse(null);
  }

  private static String stripIndicators(String toStrip, String escapedIndicator) {
    return ParserUtils
        .replaceFirst(escapedIndicator, toStrip, "");
  }

  private static String regexEscape(String toEscape) {
    return "\\" + toEscape;
  }

}
