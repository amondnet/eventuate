package net.amond.eventuate.messaging;

import net.amond.eventuate.common.Event;

/**
 * Created by amond on 17. 3. 16.
 *
 * Defines that the object exposes events that are meant to be published.
 */
public interface EventPublisher {

  Iterable<Event> events();
}
