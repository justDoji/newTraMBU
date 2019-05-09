package be.doji.productivity.trambu.infrastructure.parser;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

import be.doji.productivity.trambu.infrastructure.parser.Property.Indicator;
import be.doji.productivity.trambu.infrastructure.parser.Property.Regex;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;

public class PropertyTest {

  @Test
  public void property_privateConstructor()
      throws NoSuchMethodException, ClassNotFoundException {
    Class<?> aClass = Class.forName("be.doji.productivity.trambu.infrastructure.parser.Property");
    Constructor<?> c = aClass.getDeclaredConstructor();
    c.setAccessible(true);

    assertThatThrownBy(aClass::newInstance).isInstanceOf(IllegalAccessException.class);
  }

  @Test
  public void regex_privateConstructor()
      throws NoSuchMethodException, ClassNotFoundException {
    Class<?> aClass = Class.forName("be.doji.productivity.trambu.infrastructure.parser.Property$Regex");
    Constructor<?> c = aClass.getDeclaredConstructor(Class.forName("be.doji.productivity.trambu.infrastructure.parser.Property"));
    c.setAccessible(true);

    assertThatThrownBy(aClass::newInstance).isInstanceOf(InstantiationException.class);
  }

  @Test
  public void indicator_privateConstructor()
      throws NoSuchMethodException, ClassNotFoundException {
    Class<?> aClass = Class.forName("be.doji.productivity.trambu.infrastructure.parser.Property$Indicator");
    Constructor<?> c = aClass.getDeclaredConstructor(Class.forName("be.doji.productivity.trambu.infrastructure.parser.Property"));
    c.setAccessible(true);

    assertThatThrownBy(aClass::newInstance).isInstanceOf(InstantiationException.class);
  }

}