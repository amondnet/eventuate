package net.amond.eventuate.messaging.handling;

import net.amond.eventuate.common.Event;

/**
 * Created by amond on 17. 3. 16.
 *
 * Marker interface that makes it easier to discover handlers via reflection.
 *
 * @author amond
 */
public interface EventHandler<T extends Event> {

  /**
   * Invoked when a specific event of the type for which this handler is registered happens.
   *
   * @param event the event which occurred
   */
  void handle(Event event);
}
