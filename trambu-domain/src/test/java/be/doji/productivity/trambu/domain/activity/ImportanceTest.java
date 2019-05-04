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