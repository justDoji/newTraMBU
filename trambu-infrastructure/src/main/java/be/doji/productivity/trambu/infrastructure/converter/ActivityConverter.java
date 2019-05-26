/**
 * TraMBU - an open time management tool
 *
 * Copyright (C) 2019  Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.infrastructure.converter;

import static be.doji.productivity.trambu.infrastructure.converter.ParserUtils.findAllMatches;
import static be.doji.productivity.trambu.infrastructure.converter.ParserUtils.findAndStripIndicators;
import static be.doji.productivity.trambu.infrastructure.converter.ParserUtils.findFirstMatch;
import static be.doji.productivity.trambu.infrastructure.converter.ParserUtils.matches;
import static be.doji.productivity.trambu.infrastructure.converter.ParserUtils.regexEscape;
import static be.doji.productivity.trambu.infrastructure.converter.ParserUtils.replaceFirst;
import static be.doji.productivity.trambu.infrastructure.converter.ParserUtils.stripGroupIndicators;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.infrastructure.converter.Property.Indicator;
import be.doji.productivity.trambu.infrastructure.converter.Property.Regex;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

// TODO: make this a service for consitency
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
        .conversionStep(ActivityConverter::parseReferenceKey, Activity::setReferenceKey)
        .getConvertedData();
  }

  private static boolean parseCompleted(String line) {
    return matches(Regex.COMPLETED, line);
  }

  private static String parseTitle(String line) {
    return stripGroupIndicators(
        findFirstMatch(Regex.TITLE, line).orElse(DEFAULT_TITLE)).trim();
  }

  private static String parseDeadline(String line) {
    return findAndStripIndicators(Indicator.DEADLINE, Regex.DEADLINE, line).orElse(null);
  }

  private static List<String> parseTags(String line) {
    List<String> tagMatches = findAllMatches(Regex.TAG, line);

    return tagMatches.stream()
        .map(ParserUtils::stripGroupIndicators)
        .map(tag -> replaceFirst(Indicator.TAG, tag, ""))
        .map(String::trim)
        .collect(Collectors.toList());
  }

  private static List<String> parseProjects(String line) {
    List<String> projectMatches = findAllMatches(Regex.PROJECT, line);

    return projectMatches.stream()
        .map(ParserUtils::stripGroupIndicators)
        .map(project -> replaceFirst(regexEscape(Indicator.PROJECT), project, ""))
        .map(String::trim)
        .collect(Collectors.toList());
  }

  private static String parseReferenceKey(String line) {
    String firstMatch = findAndStripIndicators(Indicator.REFERENCE_KEY, Regex.REFERENCE_KEY, line)
        .orElse(UUID.randomUUID().toString());
    return stripGroupIndicators(firstMatch).trim();
  }

  private static class ActivityToStringConverter extends Converter<Activity, StringBuilder> {

    ActivityToStringConverter(Activity source) {
      super(source, StringBuilder.class);
    }
  }

  public static String write(Activity activityToParse) {
    return new ActivityToStringConverter(activityToParse)
        .conversionStep(ActivityConverter::writeCompleted, StringBuilder::append)
        .conversionStep(ActivityConverter::writeTitle, StringBuilder::append)
        .conversionStep(ActivityConverter::writeDeadline, StringBuilder::append)
        .conversionStep(ActivityConverter::writeTags, StringBuilder::append)
        .conversionStep(ActivityConverter::writeProjects, StringBuilder::append)
        .conversionStep(ActivityConverter::writeReferenceKey, StringBuilder::append)
        .getConvertedData()
        .toString();
  }

  private static String writeReferenceKey(Activity activity) {
    return Indicator.REFERENCE_KEY + Indicator.GROUP_START + activity.getReferenceKey()
        + Indicator.GROUP_END;
  }

  private static String writeCompleted(Activity activity) {
    return activity.isCompleted() ? Indicator.DONE + " " : "";
  }

  private static String writeTitle(Activity activity) {
    return Indicator.GROUP_START + activity.getTitle() + Indicator.GROUP_END + " ";
  }

  private static String writeDeadline(Activity activity) {
    Optional<TimePoint> deadline = activity.getDeadline();
    return deadline.map(timePoint -> Indicator.DEADLINE + Property.LEGACY_FORMATTER
        .format(timePoint.toLocalDateTime())
        + " ").orElse("");
  }

  private static String writeTags(Activity activity) {
    return activity.getTags().stream()
        .map(s -> Indicator.TAG + Indicator.GROUP_START + s + Indicator.GROUP_END + " ").collect(
            Collectors.joining());
  }

  private static String writeProjects(Activity activity) {
    return activity.getProjects().stream()
        .map(s -> Indicator.PROJECT + Indicator.GROUP_START + s + Indicator.GROUP_END + " ")
        .collect(
            Collectors.joining());
  }
}
