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


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ImportanceTest {

  @Test
  public void importance_compare_identity() {
    assertThat(Importance.HIGH.compareTo(Importance.HIGH)).isEqualTo(0);
    assertThat(Importance.NORMAL.compareTo(Importance.NORMAL)).isEqualTo(0);
    assertThat(Importance.LOW.compareTo(Importance.LOW)).isEqualTo(0);
  }

  @Test
  public void importance_compare_order() {
    assertThat(Importance.LOW.compareTo(Importance.NORMAL)).isEqualTo(-1);
    assertThat(Importance.NORMAL.compareTo(Importance.HIGH)).isEqualTo(-1);
  }

  @Test
  public void importance_compare_transitive() {
    assertThat(Importance.LOW.compareTo(Importance.HIGH)).isEqualTo(-1);
  }

  @Test
  public void importance_compare_symmetry() {
    assertThat(Importance.NORMAL.compareTo(Importance.LOW)).isEqualTo(1);
    assertThat(Importance.HIGH.compareTo(Importance.NORMAL)).isEqualTo(1);

    //Transitive symmetry
    assertThat(Importance.HIGH.compareTo(Importance.LOW)).isEqualTo(1);
  }

  @Test
  public void importance_equality() {
    assertThat(Importance.NORMAL.equals(Importance.HIGH)).isFalse();
    assertThat(Importance.HIGH.equals(Importance.NORMAL)).isFalse();
    assertThat(Importance.LOW.equals(Importance.NORMAL)).isFalse();
    assertThat(Importance.LOW.equals(Importance.HIGH)).isFalse();
    assertThat(Importance.NORMAL.equals(Importance.NORMAL)).isTrue();
  }

  @Test
  public void importance_hash() {
    assertThat(Importance.NORMAL.hashCode()).isEqualTo(Importance.NORMAL.hashCode());
    assertThat(Importance.NORMAL.hashCode()).isNotEqualTo(Importance.HIGH.hashCode());
    assertThat(Importance.NORMAL.hashCode()).isNotEqualTo(Importance.LOW.hashCode());
  }

}