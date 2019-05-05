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
package be.doji.productivity.trambu.domain.activity;

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
