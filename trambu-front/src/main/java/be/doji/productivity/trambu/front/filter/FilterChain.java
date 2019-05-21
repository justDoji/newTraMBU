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
package be.doji.productivity.trambu.front.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FilterChain<T> {

  private List<PositiveFilter> filters = new ArrayList<>();

  public <P> void addPositiveFiler(Function<T, P> supplier, Function<P, Boolean> includeWhen) {
    this.filters.add(new PositiveFilter(supplier, includeWhen));
  }

  public List<T> getFilteredData(List<T> initialList) {
    if (filters.isEmpty()) {
      return initialList;
    }

    List<T> results = new ArrayList<>();
    for (T element : initialList) {
      for (PositiveFilter filter : this.filters) {
        if (filter.allows(element)) {
          results.add(element);
          break;
        }
      }
    }
    return results;
  }

  class PositiveFilter<T, P> {

    private final Function<T, P> supplier;
    private final Function<P, Boolean> isTobeAddedWhen;

    public PositiveFilter(Function<T, P> supplier, Function<P, Boolean> isTobeAddedWhen) {
      this.isTobeAddedWhen = isTobeAddedWhen;
      this.supplier = supplier;
    }

    public boolean allows(T elementToCheck) {
      return isTobeAddedWhen.apply(supplier.apply(elementToCheck));
    }
  }

}
