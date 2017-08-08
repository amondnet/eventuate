package net.amond.eventuate;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class IterableUtil {
  public static <T> Iterable<T> where(Iterable<T> items, Predicate<T> predicate) {
    ArrayList<T> result = new ArrayList<T>();
    for (T item : items) {
      if (predicate.test(item)) {
        result.add(item);
      }
    }
    return result;
  }

  public static <T, R> Iterable<R> select(Iterable<T> items, Function<T, R> func) {
    ArrayList<R> result = new ArrayList<R>();
    for (T item : items) {
      result.add(func.apply(item));
    }
    return result;
  }
}