/**
 * TraMBU - an open time management tool
 * <p>
 * Copyright (C) 2019  Stijn Dejongh
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * <p>
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.planning.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Tag {

  private String name;

  public Tag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Tag tag = (Tag) o;

    return new EqualsBuilder()
        .append(name, tag.name)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(name)
        .toHashCode();
  }
}
