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

}
