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
package be.doji.productivity.trambu.zulma.domain.activity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Importance implements Comparable<Importance> {

  public static final Importance LOW = new Importance(10);
  public static final Importance NORMAL = new Importance(50);
  public static final Importance HIGH = new Importance(100);

  private static final int MAX_SCALE_VALUE = 120;
  private static final int MIN_SCALE_VALUE = 0;

  private int innerValue;

  public Importance(int relativeImportance) {

    this.innerValue = scaleImportance(relativeImportance);
  }

  private int scaleImportance(int toScale) {
    return Math.min(Math.max(toScale, MIN_SCALE_VALUE), MAX_SCALE_VALUE);
  }

  @Override
  public int compareTo(Importance o) {
    if (this == o) {
      return 0;
    }

    return Integer.compare(this.innerValue, o.innerValue);
  }

  /* Reference for overriding equals and hashcode:
   * https://stackoverflow.com/questions/27581/what-issues-should-be-considered-when-overriding-equals-and-hashcode-in-java
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Importance)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    return new EqualsBuilder()
        .append(innerValue, ((Importance) obj).innerValue)
        .isEquals();

  }

  /* Reference for overriding equals and hashcode:
   * https://stackoverflow.com/questions/27581/what-issues-should-be-considered-when-overriding-equals-and-hashcode-in-java
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
        // if deriving: appendSuper(super.hashCode()).
            append(innerValue).
            toHashCode();
  }
}
