package net.amond.eventuate.messaging.handling;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.amond.eventuate.common.Event;
import net.amond.eventuate.messaging.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by amond on 17. 3. 16.
 *
 * @author amond
 */
public class EventDispatcher {
  private static Logger logger = LoggerFactory.getLogger(EventDispatcher.class);

  private final String subscriberId;
  private final Map<Class<?>, EventHandler> eventTypesAndHandlers;

  public EventDispatcher(String subscriberId, Map<Class<?>, EventHandler> eventTypesAndHandlers) {
    this.subscriberId = subscriberId;
    this.eventTypesAndHandlers = eventTypesAndHandlers;
  }

  public CompletableFuture<?> dispatch(Envelope<Event> eventEnvelope) {
    EventHandler eventHandler = eventTypesAndHandlers.get(eventEnvelope.body().getClass());
    if (eventHandler != null) {
      // TODO
      /*
      if (activityLogger.isDebugEnabled()) {
        activityLogger.debug("Invoking event handler {} {} {}", subscriberId, de, eventHandler);
        return CompletableFutureUtil.tap(eventHandler.dispatch(de), (result, throwable) -> {
          if (throwable == null) {
            activityLogger.debug("Invoked event handler {} {} {}", subscriberId, de, eventHandler);
          } else {
            activityLogger.debug(
                String.format("Event handler failed %s %s %s", subscriberId, de, eventHandler),
                throwable);
          }
        });
      } else {
      */
      // return eventHandler.handle(eventEnvelope.body());
      return null;
      //}
    } else {
      RuntimeException ex = new RuntimeException(
          "No handler for event - subscriberId: " + subscriberId + ", " + eventEnvelope.body()
              .getClass()
              .getSimpleName());
      logger.error("dispatching failure", ex);
      CompletableFuture completableFuture = new CompletableFuture();
      completableFuture.completeExceptionally(ex);
      return completableFuture;
    }
  }
}
