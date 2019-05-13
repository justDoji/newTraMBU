package be.doji.productivity.trambu.infrastructure.parser;

import be.doji.productivity.trambu.infrastructure.parser.Property.Indicator;
import be.doji.productivity.trambu.infrastructure.parser.Property.Regex;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogParser {


  private static ActivityDatabaseRepository activityDatabase;

  /*  Being fancy with injects into static fields   */
  private LogParser(@Autowired ActivityDatabaseRepository repository) {
    LogParser.activityDatabase = repository;
  }

  public static LogPointData parse(String line) {
    return new LogConverter(line)
        .conversionStep(LogParser::parseStart, LogPointData::setStart)
        .conversionStep(LogParser::parseEnd, LogPointData::setEnd)
        .conversionStep(LogParser::parseActivity, LogPointData::setActivity)
        .getConvertedData();
  }

  private static String parseStart(String logline) {
    Optional<String> match = ParserUtils.findFirstMatch(Regex.LOGPOINT_START, logline);
    return ParserUtils.replaceFirst(Indicator.LOGPOINT_START, match.orElse(""), "").trim();
  }

  private static String parseEnd(String logline) {
    Optional<String> match = ParserUtils.findFirstMatch(Regex.LOGPOINT_END, logline);
    return ParserUtils.replaceFirst(Indicator.LOGPOINT_END, match.orElse(""), "").trim();
  }

  private static ActivityData parseActivity(String logline) {
    Optional<String> match = ParserUtils.findFirstMatch(Regex.LOGPOINT_ACTIVITY, logline);
    if (match.isPresent()) {
      String trimmedMatch = ParserUtils.replaceFirst(Indicator.LOGPOINT_ACTIVITY, match.get(), "")
          .trim();
      long activityId = Long.parseLong(trimmedMatch);
      return activityDatabase.findById(activityId).orElse(null);
    }

    return null;
  }

  private static class LogConverter extends Converter<String, LogPointData> {

    LogConverter(String source) {
      super(source, LogPointData.class);
    }
  }

}
