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
package be.doji.productivity.trambu.infrastructure.parser;

import be.doji.productivity.trambu.infrastructure.parser.Property.Indicator;
import be.doji.productivity.trambu.infrastructure.parser.Property.Regex;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityProjectData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityTagData;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    return new ActivityAssembler(line)
        .assembleStep(ActivityParser::parseCompleted, ActivityData::setCompleted)
        .assembleStep(ActivityParser::parseTitle, ActivityData::setTitle)
        .assembleStep(ActivityParser::parseDeadline, ActivityData::setDeadline)
        .assembleStep(ActivityParser::parseTags, ActivityData::setTags)
        .assembleStep(ActivityParser::parseProjects, ActivityData::setProjects)
        .getAssembledData();
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

  private static List<ActivityTagData> parseTags(String line) {
    List<String> tagMatches = ParserUtils.findAllMatches(Regex.TAG, line);

    return tagMatches.stream()
        .map(ActivityParser::stripIndicators)
        .map(tag -> ParserUtils.replaceFirst(Indicator.TAG, tag, ""))
        .map(String::trim)
        .map(ActivityTagData::new)
        .collect(Collectors.toList());
  }

  private static List<ActivityProjectData> parseProjects(String line) {
    List<String> projectMatches = ParserUtils.findAllMatches(Regex.PROJECT, line);

    return projectMatches.stream()
        .map(ActivityParser::stripIndicators)
        .map(project -> ParserUtils.replaceFirst(regexEscape(Indicator.PROJECT), project, ""))
        .map(String::trim)
        .map(ActivityProjectData::new)
        .collect(Collectors.toList());
  }

  private static class ActivityAssembler {

    private ActivityData activityData;
    private String line;

    ActivityAssembler(String lineToParse) {
      this.line = lineToParse;
      activityData = new ActivityData();
    }

    public <T> ActivityAssembler assembleStep(Function<String, T> parsingFunction,
        BiConsumer<ActivityData, T> setter) {
      setter.accept(activityData, parsingFunction.apply(line));
      return this;
    }

    public ActivityData getAssembledData() {
      return activityData;
    }
  }

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
