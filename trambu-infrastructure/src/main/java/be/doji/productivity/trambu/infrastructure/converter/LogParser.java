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
package be.doji.productivity.trambu.infrastructure.converter;

import be.doji.productivity.trambu.infrastructure.converter.Property.Indicator;
import be.doji.productivity.trambu.infrastructure.converter.Property.Regex;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogParser {

  private ActivityDatabaseRepository activityDatabase;

  public LogParser(@Autowired ActivityDatabaseRepository repository) {
    this.activityDatabase = repository;
  }

  public LogPointData parse(String line) {
    return new LogConverter(line)
        .conversionStep(this::parseStart, LogPointData::setStart)
        .conversionStep(this::parseEnd, LogPointData::setEnd)
        .conversionStep(this::parseActivity, LogPointData::setActivity)
        .getConvertedData();
  }

  private String parseStart(String logline) {
    Optional<String> match = ParserUtils.findFirstMatch(Regex.LOGPOINT_START, logline);
    return ParserUtils.replaceFirst(Indicator.LOGPOINT_START, match.orElse(""), "").trim();
  }

  private String parseEnd(String logline) {
    Optional<String> match = ParserUtils.findFirstMatch(Regex.LOGPOINT_END, logline);
    return ParserUtils.replaceFirst(Indicator.LOGPOINT_END, match.orElse(""), "").trim();
  }

  private ActivityData parseActivity(String logline) {
    Optional<String> match = ParserUtils.findFirstMatch(Regex.LOGPOINT_ACTIVITY, logline);
    if (match.isPresent()) {
      String trimmedMatch = ParserUtils.replaceFirst(Indicator.LOGPOINT_ACTIVITY, match.get(), "")
          .trim();
      long activityId = Long.parseLong(trimmedMatch);
      return activityDatabase.findById(activityId).orElse(null);
    }

    return null;
  }

  private class LogConverter extends Converter<String, LogPointData> {

    LogConverter(String source) {
      super(source, LogPointData.class);
    }
  }

}
