package be.doji.productivity.trambu.timetracking.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pair<T, S> {

  private T first;
  private S seccond;

  public static <L, R> Pair<L, R> of(final L left, final R right) {
    return new Pair<>(left, right);
  }
}
