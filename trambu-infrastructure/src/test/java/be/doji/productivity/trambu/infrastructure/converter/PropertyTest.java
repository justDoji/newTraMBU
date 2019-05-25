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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Constructor;
import org.junit.Test;

public class PropertyTest {

  @Test
  public void property_privateConstructor()
      throws NoSuchMethodException, ClassNotFoundException {
    Class<?> aClass = Class.forName("be.doji.productivity.trambu.infrastructure.converter.Property");
    Constructor<?> c = aClass.getDeclaredConstructor();
    c.setAccessible(true);

    assertThatThrownBy(aClass::newInstance).isInstanceOf(IllegalAccessException.class);
  }

  @Test
  public void regex_privateConstructor()
      throws NoSuchMethodException, ClassNotFoundException {
    Class<?> aClass = Class
        .forName("be.doji.productivity.trambu.infrastructure.converter.Property$Regex");
    Constructor<?> c = aClass.getDeclaredConstructor(
        Class.forName("be.doji.productivity.trambu.infrastructure.converter.Property"));
    c.setAccessible(true);

    assertThatThrownBy(aClass::newInstance).isInstanceOf(InstantiationException.class);
  }

  @Test
  public void indicator_privateConstructor()
      throws NoSuchMethodException, ClassNotFoundException {
    Class<?> aClass = Class
        .forName("be.doji.productivity.trambu.infrastructure.converter.Property$Indicator");
    Constructor<?> c = aClass.getDeclaredConstructor(
        Class.forName("be.doji.productivity.trambu.infrastructure.converter.Property"));
    c.setAccessible(true);

    assertThatThrownBy(aClass::newInstance).isInstanceOf(InstantiationException.class);
  }

}