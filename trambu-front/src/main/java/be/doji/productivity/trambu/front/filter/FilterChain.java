/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
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
 * If not, see <http://www.gnu.org/licenses/>.
 */
package be.doji.productivity.trambu.front.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterChain<T> {

  private List<PositiveFilter> filters = new ArrayList<>();

  public <P> void addPositiveFiler(String displayValue, Function<T, P> supplier,
      Predicate<P> includeWhen, String type) {
    List<String> typeNames = this.filters.stream()
        .map(filter -> createIdentifier(filter.getName(), filter.getType())).collect(
            Collectors.toList());
    if (!typeNames.contains(createIdentifier(displayValue, type))) {
      this.filters.add(new PositiveFilter(displayValue, supplier, includeWhen, type));
    }
  }

  private String createIdentifier(String displayValue, String type) {
    return type + ":" + displayValue;
  }

  public void reset() {
    this.filters = new ArrayList<>();
  }

  public List<T> getFilteredData(List<T> initialList) {
    if (filters.isEmpty()) {
      return initialList;
    }

    List<T> results = new ArrayList<>();
    for (T element : initialList) {
      if (shouldBeIncluded(element)) {
        results.add(element);
      }
    }
    return results;
  }

  private boolean shouldBeIncluded(T element) {
    for (PositiveFilter filter : this.filters) {
      if (!filter.allows(element)) {
        return false;
      }
    }
    return true;
  }

  public List<PositiveFilter> getFilters() {
    return filters;
  }

  public void removeFilter(PositiveFilter filter) {
    this.filters.remove(filter);
  }

  public void clearFilters() {
    this.filters.clear();
  }

  public class PositiveFilter<T, P> {

    private final Function<T, P> supplier;
    private final Predicate<P> isTobeAddedWhen;
    private final String name;
    private final String clazz;

    public PositiveFilter(String displayValue, Function supplier,
        Predicate isTobeAddedWhen, String clazz) {
      this.name = displayValue;
      this.isTobeAddedWhen = isTobeAddedWhen;
      this.supplier = supplier;
      this.clazz = clazz;
    }

    public boolean allows(T elementToCheck) {
      return isTobeAddedWhen.test(supplier.apply(elementToCheck));
    }

    public String getName() {
      return name;
    }

    public Predicate<P> getPredicate() {
      return isTobeAddedWhen;
    }

    public String getType() {
      return clazz;
    }
  }

}
