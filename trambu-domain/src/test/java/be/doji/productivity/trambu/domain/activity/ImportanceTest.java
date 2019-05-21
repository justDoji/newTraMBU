/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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