package be.doji.productivity.trambu.infrastructure.parser;

import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;

public class LogParser {

  public static LogPointData parse(String line) {

    return new LogConverter(line)
        .conversionStep(LogParser::parseStart, LogPointData::setStart)
        .conversionStep(LogParser::parseEnd, LogPointData::setStart)
        .conversionStep(LogParser::parseActivity, LogPointData::setActivity)
        .getConvertedData();
  }

  private static String parseStart(String s) {
    return null;
  }

  private static String parseEnd(String s) {
    return null;
  }

  private static ActivityData parseActivity(String s) {
    return null;
  }

  private static class LogConverter extends Converter<String, LogPointData> {
    LogConverter(String source) {
      super(source, LogPointData.class);
    }
  }

}
