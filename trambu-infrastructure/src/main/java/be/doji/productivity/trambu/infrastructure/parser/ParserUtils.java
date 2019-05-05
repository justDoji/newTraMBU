package be.doji.productivity.trambu.infrastructure.parser;

import java.util.ArrayList;
import java.util.List;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import org.apache.commons.lang3.StringUtils;

public final class ParserUtils {

  /*Utility classes should not have a public or default constructor */
  private ParserUtils() {
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

}
