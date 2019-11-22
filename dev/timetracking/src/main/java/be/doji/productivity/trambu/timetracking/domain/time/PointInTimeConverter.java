/**
 * TraMBU - an open time management tool
 *
 *     Copyright (C) 2019  Stijn Dejongh
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     For further information on usage, or licensing, contact the author
 *     through his github profile: https://github.com/justDoji
 */
package be.doji.productivity.trambu.timetracking.domain.time;

import static be.doji.productivity.trambu.timetracking.domain.time.TimeFormatSpecification.EXTENDED_DATE_TIME;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class PointInTimeConverter extends BidirectionalConverter<PointInTime, String> {

  @Override
  public String convertTo(PointInTime source, Type<String> destinationType,
      MappingContext mappingContext) {
    return EXTENDED_DATE_TIME.string(source);
  }

  @Override
  public PointInTime convertFrom(String source, Type<PointInTime> destinationType,
      MappingContext mappingContext) {
    return TimeFormatSpecification.parse(source);
  }
}
