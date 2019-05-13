package be.doji.productivity.trambu.infrastructure.parser;

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
