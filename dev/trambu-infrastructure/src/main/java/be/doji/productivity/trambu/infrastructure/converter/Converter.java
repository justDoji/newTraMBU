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
package be.doji.productivity.trambu.infrastructure.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Converter<F, T> {

  private final F source;
  private T target;

  public Converter(F source, Class<T> aClass) {
    try {
      this.source = source;
      Constructor<T> declaredConstructor = aClass.getDeclaredConstructor();
      this.target = declaredConstructor.newInstance();
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
      throw new IllegalArgumentException("Error while creating converter: " + e.getMessage());
    }
  }

  public <I extends Object> Converter<F, T> conversionStep(Function<F, I> parsingFunction,
      BiConsumer<T, I> setter) {
    setter.accept(target, parsingFunction.apply(source));
    return this;
  }

  public T getConvertedData() {
    return target;
  }
}
