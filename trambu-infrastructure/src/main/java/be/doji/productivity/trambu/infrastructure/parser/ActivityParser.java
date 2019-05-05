package be.doji.productivity.trambu.infrastructure.parser;

import be.doji.productivity.trambu.infrastructure.transfer.ActivityTO;
import org.apache.commons.lang3.StringUtils;

public final class ActivityParser {

  /* Utility classes should not have a public or default constructor */
  private ActivityParser() {
  }

  public static ActivityTO parse(String line) {
    if (StringUtils.isBlank(line)) {
      throw new IllegalArgumentException(
          "Failure during parsing: empty String or null value not allowed");
    }
    ActivityTO activity = new ActivityTO();

    //TODO: parse here

    return activity;
  }
}
