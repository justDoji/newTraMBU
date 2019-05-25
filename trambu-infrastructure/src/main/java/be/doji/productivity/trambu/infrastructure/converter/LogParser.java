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

import static be.doji.productivity.trambu.infrastructure.converter.ParserUtils.findFirstMatch;
import static be.doji.productivity.trambu.infrastructure.converter.ParserUtils.replaceFirst;
import static be.doji.productivity.trambu.infrastructure.converter.ParserUtils.stripGroupIndicators;

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
    Optional<String> match = findFirstMatch(Regex.LOGPOINT_START, logline);
    return replaceFirst(Indicator.LOGPOINT_START, match.orElse(""), "").trim();
  }

  private String parseEnd(String logline) {
    Optional<String> match = findFirstMatch(Regex.LOGPOINT_END, logline);
    return replaceFirst(Indicator.LOGPOINT_END, match.orElse(""), "").trim();
  }

  private ActivityData parseActivity(String logline) {
    Optional<String> match = findFirstMatch(Regex.LOGPOINT_ACTIVITY, logline);
    if (match.isPresent()) {
      String trimmedMatch = stripGroupIndicators(
          replaceFirst(Indicator.LOGPOINT_ACTIVITY, match.get(), "")).trim();
      return activityDatabase.findByReferenceKey(trimmedMatch).orElse(null);
    }

    return null;
  }

  private class LogConverter extends Converter<String, LogPointData> {

    LogConverter(String source) {
      super(source, LogPointData.class);
    }
  }

}
