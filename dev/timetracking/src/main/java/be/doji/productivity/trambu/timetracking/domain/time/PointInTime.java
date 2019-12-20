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
package be.doji.productivity.trambu.timetracking.domain.time;

import be.doji.productivity.trambu.kernel.annotations.ValueObject;
import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@ValueObject
public final class PointInTime {

  private final LocalDateTime internalDateTime;

  public PointInTime(LocalDateTime temporal) {
    this.internalDateTime = temporal;
  }

  public static PointInTime parse(String toParse) {
    return TimeFormatSpecification.parse(toParse);
  }

  public static PointInTime fromDateTime(LocalDateTime dateTime) {return new PointInTime(dateTime);}

  public String toString() {
    return TimeFormatSpecification.EXTENDED_DATE_TIME.string(this);
  }

  public LocalDateTime dateTime() {
    return this.internalDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }

    if (o == null || getClass() != o.getClass()) { return false; }

    PointInTime that = (PointInTime) o;

    return new EqualsBuilder()
        .append(internalDateTime, that.internalDateTime)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(internalDateTime)
        .toHashCode();
  }
}
