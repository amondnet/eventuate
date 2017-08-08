package net.amond.eventuate.common;

import java.util.Collection;
import java.util.UUID;

/**
 * Base interface for an Aggregate that uses event sourcing
 *
 * @param <T> the aggregate class
 */
public interface Aggregate<T extends Aggregate> {

  /**
   * Update the aggregate
   *
   * @param event the event representing the state change
   * @return the updated aggregate, which might be this
   */
  T applyEvent(Event event);

  /**
   * Gets the entity identifier.
   */
  UUID id();

  /**
   * Gets the collection of new events since the entity was loaded, as a consequence of command
   * handling.
   */
  Collection<Event> getUncommittedEvents();

  /**
   * Gets the entity's version. As the entity is being updated and events being generated, the
   * version is incremented.
   */
  int version();
}