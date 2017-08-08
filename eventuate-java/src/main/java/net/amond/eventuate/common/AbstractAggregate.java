package net.amond.eventuate.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.amond.eventuate.messaging.handling.EventHandler;

/**
 * @author Minsu Lee
 */
public abstract class AbstractAggregate implements Aggregate {

  private UUID id;

  private Map<Class<?>, EventHandler> handlers = new HashMap<>();
  private List<Event> uncommittedEvents = new LinkedList<>();
  private int version;

  protected AbstractAggregate(UUID id) {
    this.id = id;
  }

  protected void id(UUID id) {
    this.id = id;
  }

  @Override public UUID id() {
    return id;
  }

  @Override public Collection<Event> getUncommittedEvents() {
    return uncommittedEvents;
  }

  protected void version(int version) {
    this.version = version;
  }

  @Override public int version() {
    return version;
  }

  @Override public Aggregate applyEvent(Event event) {
    version++;
    //handlers.get(event.getClass()).handle(event);
    return this;
  }

  protected void raiseEvent(Event event) {
    applyEvent(event);
    uncommittedEvents.add(event);
  }

  protected void handles(Class clz, EventHandler handler) {
    this.handlers.put(clz, handler);
  }
}
