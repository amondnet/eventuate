package net.amond.eventuate.common;

/**
 * An opaque object that contains the state of another object (a snapshot) and can be used to
 * restore its state.
 *
 * @author amond
 */
public interface Memento {

  /**
   * The version of the {@link Aggregate} instance
   */
  int version();
}
