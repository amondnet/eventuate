package net.amond.eventuate.messaging;

import net.amond.eventuate.common.Event;
import net.amond.eventuate.getevenstore.GetEventStoreRepository;

/**
 * Created by amond on 17. 3. 16.
 *
 * An event bus that sends serialized object payloads.
 *
 * Note that
 * {@link net.amond.eventuate.common.Aggregate}
 * entities persisted through the {@link GetEventStoreRepository}.
 * do not use the {@link EventBus}, but has its own event publishing mechanism.
 */
public interface EventBus {
  void publish(Envelope<Event> event);

  void publish(Iterable<Envelope<Event>> events);
}
