/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package be.doji.productivity.trambu.infrastructure.converter;

import be.doji.productivity.trambu.infrastructure.converter.Property.Indicator;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public final class ParserUtils {

  /*Utility classes should not have a public or default constructor */
  private ParserUtils() {
    throw new UnsupportedOperationException();
  }

  static List<String> findAllMatches(String regex, String lineToSearch) {
    if (StringUtils.isBlank(regex)) {
      throw new IllegalArgumentException("I will not search for an empty String");
    }
    if (StringUtils.isBlank(lineToSearch)) {
      return new ArrayList<>();
    }

    List<String> allMatches = new ArrayList<>();
    Matcher m = Pattern.compile(regex).matcher(lineToSearch);
    while (m.find()) {
      allMatches.add(m.group());
    }

    return allMatches;
  }

  static String escape(String input) {
    if (input == null) {
      throw new IllegalArgumentException("I will not escape a null value");
    }
    return "\"" + input + "\"";
  }

  static boolean matches(String regex, String line) {
    return !findAllMatches(regex, line).isEmpty();
  }

  public static Optional<String> findFirstMatch(String regex, String line) {
    List<String> matches = findAllMatches(regex, line);
    return matches.isEmpty() ? Optional.empty() : Optional.of(matches.get(0));
  }

  public static String replaceFirst(String regex, String line, String replacement) {
    Optional<String> firstMatch = findFirstMatch(regex, line);
    if(firstMatch.isPresent()) {
      String toReplace = firstMatch.get();
      int pos = line.indexOf(toReplace);
      return line.substring(0, pos)
          + replacement
          + line.substring(pos + toReplace.length());
    } else {
      return line;
    }
  }

  public static String replaceLast(String regex, String line, String replacement) {
    Optional<String> firstMatch = findFirstMatch(regex, line);
    if(firstMatch.isPresent()) {
      String toReplace = firstMatch.get();
      int pos = line.lastIndexOf(toReplace);
      return line.substring(0, pos)
          + replacement
          + line.substring(pos + toReplace.length());
    } else {
      return line;
    }
  }

  public static String stripGroupIndicators(String toStrip) {
    String strippedMatch = ParserUtils
        .replaceFirst(regexEscape(Indicator.GROUP_START), toStrip, "");
    return ParserUtils.replaceLast(regexEscape(Indicator.GROUP_END), strippedMatch, "");
  }

  public static Optional<String> findAndStripIndicators(String escapedIndicator, String regex,
      String line) {
    return ParserUtils
        .findFirstMatch(regex, line)
        .map(s -> stripIndicators(s, escapedIndicator).trim());
  }

  public static String stripIndicators(String toStrip, String escapedIndicator) {
    return ParserUtils
        .replaceFirst(escapedIndicator, toStrip, "");
  }

  public static String regexEscape(String toEscape) {
    return "\\" + toEscape;
  }

}
