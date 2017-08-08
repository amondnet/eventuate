package net.amond.eventuate.messaging.handling;

/**
 * Created by amond on 17. 3. 16.
 *
 * @author amond
 */
public interface EventHandlerRegistry {
  void register(EventHandler handler);
}
